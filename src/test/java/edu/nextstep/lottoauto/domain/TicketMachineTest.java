package edu.nextstep.lottoauto.domain;

import edu.nextstep.lottoauto.exception.PaymentIllegalArgumentException;
import edu.nextstep.lottoauto.domain.ticketmaker.AutoNumbersMaker;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.util.List;

import static org.assertj.core.api.Assertions.*;

public class TicketMachineTest {

    private static final int TICKET_PRICE = TicketMachine.TICKET_PRICE;

    @Test
    @DisplayName("Tickets 생성")
    void createAndSaveTickets() {
        // given
        int payment = 10000;

        // when
        List<Ticket> tickets = TicketMachine.createTickets(payment, new AutoNumbersMaker());

        // then
        assertThat(tickets.size()).isEqualTo(payment/TICKET_PRICE);
        assertThat(tickets.get(0)).isInstanceOf(Ticket.class);
    }

    @ParameterizedTest(name = "금액 검증 실패 : " + TICKET_PRICE + "원 미만")
    @ValueSource(ints = {1, TICKET_PRICE-1})
    void validate_createAndSaveTickets_단위(int payment) {
        // when
        assertThatThrownBy(() -> TicketMachine.createTickets(payment, new AutoNumbersMaker()))
                .isInstanceOf(PaymentIllegalArgumentException.class)
                .hasMessageContaining("최소 입력 가능 금액 미달.");
    }

    @ParameterizedTest(name = "금액 검증 실패 : " + TICKET_PRICE + "원 단위 떨어지지 않음")
    @ValueSource(ints = {TICKET_PRICE+1, 2*TICKET_PRICE-1})
    void validate_createAndSaveTickets_1000미만(int payment) {
        // when, then
        assertThatThrownBy(() -> TicketMachine.createTickets(payment, new AutoNumbersMaker()))
                .isInstanceOf(PaymentIllegalArgumentException.class)
                .hasMessageContaining("개 당 금액");
    }
}
