package lotto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static lotto.model.Lotto.INVALID_LOTTO_NUMBERS;
import static lotto.model.LottoNumber.INVALID_LOTTO_NUMBER;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class LottoTest {
    private static final int VALID_LOTTO_LENGTH = 6;
    private static final int LOTTO_START_NUMBER = 1;
    private static final int LOTTO_END_NUMBER = 45;

    private static final List<String> VALID_NUMBERS = List.of("1","2","3","4","5","6");
    private static final List<String> INVALID_NUMBERS = List.of("-1","2","3","4","5","111");
    private static final List<String> INVALID_COUNT_OF_NUMBERS = List.of("1","2","3","4","5","6","7");

    @Test
    @DisplayName("String 인자로 로또 생성 성공")
    void testLotto_ValidNumbersAsString_ShouldConstructCorrectLotto() {
        // when
        Lotto lotto = Lotto.createLottoFrom(VALID_NUMBERS);
        Set<LottoNumber> lottoNumbers = lotto.getLotto();

        // then
        assertThat(lottoNumbers.size()).isEqualTo(VALID_LOTTO_LENGTH);
        for (LottoNumber lottoNumber : lottoNumbers) {
            assertThat(lottoNumber.getLottoNumber()).isBetween(LOTTO_START_NUMBER, LOTTO_END_NUMBER);
        }
    }

    @Test
    @DisplayName("String 인자로 로또 생성 실패 : 잘못된 값")
    void testLotto_InValidNumbersAsString_ShouldThrowException() {
        // when, then
        assertThatThrownBy(() -> Lotto.createLottoFrom(INVALID_NUMBERS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_LOTTO_NUMBER);
    }

    @Test
    @DisplayName("String 인자로 로또 생성 실패 : 잘못된 번호 개수")
    void testLotto_InValidCountOfNumbersAsString_ShouldThrowException() {
        // when, then
        assertThatThrownBy(() -> Lotto.createLottoFrom(INVALID_COUNT_OF_NUMBERS))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_LOTTO_NUMBERS);
    }
}
