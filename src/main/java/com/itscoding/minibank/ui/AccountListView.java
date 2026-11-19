package com.itscoding.minibank.ui;
 
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.ColumnTextAlign;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.data.renderer.NumberRenderer;
import com.vaadin.flow.router.Route;
import com.itscoding.minibank.domain.TransactionType;
import com.itscoding.minibank.dto.AccountResponseDto;
import com.itscoding.minibank.dto.TransactionResponseDto;
import com.itscoding.minibank.service.AccountService;
import com.itscoding.minibank.service.CustomerService;
import com.itscoding.minibank.service.TransferFacade;

import java.text.NumberFormat;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Route(value = "accounts", layout = MainLayout.class)
public class AccountListView extends VerticalLayout {
 
    private final AccountService accountService;
    // 🔥 계좌 개설 Dialog에 고객 목록을 넘기기 위해 추가
    private final CustomerService customerService;
    private final TransferFacade transferFacade;   // 🔥 12.2절 이체 화면용
    private final Grid<AccountResponseDto> grid = new Grid<>(AccountResponseDto.class, false);
    private final NumberFormat won = NumberFormat.getIntegerInstance(Locale.KOREA);   // 30,000 형식
 
    public AccountListView(AccountService accountService, CustomerService customerService,
                           TransferFacade transferFacade) {
        this.accountService = accountService;
        this.customerService = customerService;
        this.transferFacade = transferFacade;
 
        grid.addColumn(AccountResponseDto::getAccountNumber).setHeader("계좌번호");
        //grid.addColumn(AccountResponseDto::getOwnerName).setHeader("예금주");
        //grid.addColumn(AccountResponseDto::getBalance).setHeader("잔액");

        grid.addColumn(new NumberRenderer<>(AccountResponseDto::getBalance, won))
                .setHeader("잔액(원)")
                .setTextAlign(ColumnTextAlign.END)            // 숫자는 오른쪽 정렬
                .setComparator(AccountResponseDto::getBalance)  // 헤더 클릭 시 금액 순 정렬
                .setSortable(true);

        grid.addColumn(AccountResponseDto::getOwnerName).setHeader("예금주").setSortable(true);
        grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);   // 줄무늬로 행 구분

        grid.setWidthFull();

        // 🔥 12.5절 행을 클릭하면 그 계좌의 거래내역 Dialog를 연다
        grid.addItemClickListener(event -> openTransactionDialog(event.getItem()));
 
        Button createButton = new Button("새 계좌 개설", e -> openCreateDialog());
        Button transferButton = new Button("계좌 이체", e -> openTransferDialog());   // 🔥 12.2절
        Button refreshButton = new Button("새로고침", e -> refreshGrid());
        Button toCustomers = new Button("고객 관리로 이동",
                e -> getUI().ifPresent(ui -> ui.navigate("customers")));
 
        add(new HorizontalLayout(createButton, transferButton, refreshButton, toCustomers), grid);
        setSizeFull();
        refreshGrid();
    }
 
    private void refreshGrid() {
        grid.setItems(accountService.getAllAccounts());
    }
 
    private void openCreateDialog() {
        // 🔥 미리 고객 목록을 조회해서 Dialog에 넘겨줌 (ComboBox 채우기용) — 9.6절에서 채운다
        var customers = customerService.getAllCustomers();
        AccountFormDialog dialog =
                new AccountFormDialog(customers, accountService, this::refreshGrid);
        dialog.open();
    }

    // 🔥 12.2절 이체 Dialog 열기
    private void openTransferDialog() {
        var accounts = accountService.getAllAccounts();
        if (accounts.size() < 2) {
            Notification.show("이체하려면 계좌가 최소 2개 이상 필요합니다.");
            return;
        }
        TransferFormDialog dialog =
                new TransferFormDialog(accounts, transferFacade, this::refreshGrid);
        dialog.open();
    }

    // 🔥 12.5절 거래내역 Dialog
    private void openTransactionDialog(AccountResponseDto account) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle(account.getAccountNumber() + " 거래내역 (" + account.getOwnerName() + ")");

        Grid<TransactionResponseDto> txGrid = new Grid<>(TransactionResponseDto.class, false);

        // 거래 구분을 한글과 색으로 — 입금은 초록, 출금은 빨강 (Lumo 테마 색 변수)
        txGrid.addComponentColumn(t -> {
            boolean deposit = t.getType() == TransactionType.DEPOSIT;
            Span label = new Span(deposit ? "입금" : "출금");
            label.getStyle().set("color", deposit
                    ? "var(--lumo-success-text-color)" : "var(--lumo-error-text-color)");
            return label;
        }).setHeader("구분").setWidth("80px").setFlexGrow(0);

        txGrid.addColumn(new NumberRenderer<>(TransactionResponseDto::getAmount, won))
                .setHeader("금액(원)").setTextAlign(ColumnTextAlign.END)
                .setWidth("110px").setFlexGrow(0);
        txGrid.addColumn(new NumberRenderer<>(TransactionResponseDto::getBalanceAfter, won))
                .setHeader("거래후 잔액(원)").setTextAlign(ColumnTextAlign.END)
                .setWidth("130px").setFlexGrow(0);

        // 날짜를 "yyyy-MM-dd HH:mm" 형태로 짧게
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        txGrid.addColumn(t -> t.getTransactedAt().format(formatter))
                .setHeader("거래일시").setAutoWidth(true).setFlexGrow(0);   // 내용 길이에 맞춰 폭 자동

        // 메모는 남는 공간을 다 쓰도록 flexGrow 부여 (폭 고정 X)
        txGrid.addColumn(TransactionResponseDto::getDescription)
                .setHeader("메모").setFlexGrow(1);

        var history = accountService.getTransactionHistory(account.getId());
        txGrid.setItems(history);
        txGrid.setWidthFull();
        txGrid.setAllRowsVisible(true);   // 데이터 개수만큼 높이 자동 조절

        if (history.isEmpty()) {
            dialog.add(new Span("아직 거래내역이 없습니다."));
        } else {
            dialog.add(txGrid);
        }
        dialog.getFooter().add(new Button("닫기", e -> dialog.close()));
        dialog.setWidth("900px");
        dialog.open();
    }
}