package lotto.model;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import static lotto.model.Lotto.INVALID_LOTTO_NUMBERS;
import static lotto.model.LottoNumber.INVALID_LOTTO_NUMBER;
import static lotto.model.WinningLotto.BONUS_NUMBER_DENIED;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class WinningLottoTest {
    private static final List<String> VALID_NUMBERS_FIRST = List.of("1","2","3","4","5","6");
    private static final List<String> VALID_NUMBERS_SECOND_WITH_BONUS_NUMBER = List.of("11","12","13","14","16","44");
    private static final List<String> VALID_NUMBERS_FOURTH_PRIZE_WITH_BONUS_NUMBER = List.of("11","12","13","14","44","45");
    private static final List<String> OUT_OF_RANGE_NUMBERS = List.of("1","2","3","4","5","6","7");
    private static final List<String> INVALID_NUMBERS = List.of("-1","-2","-3","-4","15","63");

    @Test
    @DisplayName("6개 모두 맞춘 로또 1개를 확인 성공")
    void testLottoWinChecker_FirstPrize_ShouldReturnCorrectWinLottos() {
        // given
        List<Lotto> userLottos = List.of(new Lotto(getValidNumbersListFirst()), new Lotto(getValidNumbersListSecond()));

        // when
        WinningLotto winningLotto = new WinningLotto(VALID_NUMBERS_FIRST, new LottoNumber(7));
        Map<PrizeEnum, Integer> winLottos = winningLotto.checkWinLottos(userLottos);

        // then
        assertThat(winLottos.size()).isEqualTo(2);
        assertThat(winLottos.get(PrizeEnum.FIRST)).isEqualTo(1);
        assertThat(winLottos.get(PrizeEnum.MISS)).isEqualTo(1);
    }

    @Test
    @DisplayName("5개 맞춘 로또 1개, 5개 + 보너스 번호를 맞춘 로또 1개를 확인 성공")
    void testLottoWinChecker_SecondAndThirdPrize_ShouldReturnCorrectWinLottos() {
        // given
        List<Lotto> userLottos = List.of(new Lotto(getValidNumbersListSecond()), new Lotto(getValidNumbersListSecondWithBonusNumber()));

        // when
        WinningLotto winningLotto = new WinningLotto(VALID_NUMBERS_SECOND_WITH_BONUS_NUMBER, new LottoNumber(45));
        Map<PrizeEnum, Integer> winLottos = winningLotto.checkWinLottos(userLottos);

        // then
        assertThat(winLottos.size()).isEqualTo(2);
        assertThat(winLottos.get(PrizeEnum.SECOND)).isEqualTo(1);
        assertThat(winLottos.get(PrizeEnum.THIRD)).isEqualTo(1);
    }

    @Test
    @DisplayName("4개 + 보너스 번호 맞춘 로또 1개")
    void testLottoWinChecker_FourthPrize_ShouldReturnCorrectWinLottos() {
        // given
        List<Lotto> userLottos = List.of(new Lotto(getValidNumbersListSecond()));

        // when
        WinningLotto winningLotto = new WinningLotto(VALID_NUMBERS_FOURTH_PRIZE_WITH_BONUS_NUMBER, new LottoNumber(10));
        Map<PrizeEnum, Integer> winLottos = winningLotto.checkWinLottos(userLottos);

        // then
        assertThat(winLottos.size()).isEqualTo(1);
        assertThat(winLottos.get(PrizeEnum.FOURTH)).isEqualTo(1);
    }

    @Test
    @DisplayName("0개 맞춘 로또 2개")
    void testLottoWinChecker_NoMatchedWinNumbers_ShouldReturnCorrectWinLottos() {
        // given
        List<Lotto> userLottos = List.of(new Lotto(getValidNumbersListFirst()), new Lotto(getValidNumbersListFirst()));

        // when
        WinningLotto winningLotto = new WinningLotto(VALID_NUMBERS_SECOND_WITH_BONUS_NUMBER, new LottoNumber(1));
        Map<PrizeEnum, Integer> winLottos = winningLotto.checkWinLottos(userLottos);

        // then
        assertThat(winLottos.size()).isEqualTo(1);
        assertThat(winLottos.get(PrizeEnum.MISS)).isEqualTo(2);
    }

    @Test
    @DisplayName("LottoWinChecker 객체 생성 실패 (당첨 번호가 제한된 개수 6개 보다 많이 입력됨)")
    void testLottoWinChecker_OutOfRangeWinNumbers_ShouldThrowException() {
        // when, then
        assertThatThrownBy(() -> new WinningLotto(OUT_OF_RANGE_NUMBERS, new LottoNumber(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_LOTTO_NUMBERS);
    }

    @Test
    @DisplayName("LottoWinChecker 객체 생성 실패 (당첨 번호가 1 ~ 45가 아닌 그 이외의 값이 입력됨)")
    void testLottoWinChecker_InvalidWinNumbers_ShouldThrowException() {
        // when, then
        assertThatThrownBy(() -> new WinningLotto(INVALID_NUMBERS, new LottoNumber(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(INVALID_LOTTO_NUMBER);
    }

    @Test
    @DisplayName("LottoWinChecker 객체 생성 실패 (보너스 번호가 당첨 번호와 중복됨)")
    void testLottoWinChecker_DuplicatedBonusNumber_ShouldThrowException() {
        // when, then
        assertThatThrownBy(() -> new WinningLotto(VALID_NUMBERS_FIRST, new LottoNumber(1)))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining(BONUS_NUMBER_DENIED);
    }

    private Set<LottoNumber> getValidNumbersListFirst() {
        return new LinkedHashSet<>(Set.of(new LottoNumber(1),
                new LottoNumber(2),
                new LottoNumber(3),
                new LottoNumber(4),
                new LottoNumber(5),
                new LottoNumber(6)));
    }

    private Set<LottoNumber> getValidNumbersListSecond() {
        return new LinkedHashSet<>(Set.of(new LottoNumber(11),
                new LottoNumber(12),
                new LottoNumber(13),
                new LottoNumber(14),
                new LottoNumber(15),
                new LottoNumber(16)));
    }

    private Set<LottoNumber> getValidNumbersListSecondWithBonusNumber() {
        return new LinkedHashSet<>(Set.of(new LottoNumber(11),
                new LottoNumber(12),
                new LottoNumber(13),
                new LottoNumber(14),
                new LottoNumber(16),
                new LottoNumber(45)));
    }
}
