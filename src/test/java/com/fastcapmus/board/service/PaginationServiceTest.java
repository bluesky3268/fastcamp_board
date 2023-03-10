package com.fastcapmus.board.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.params.provider.Arguments.arguments;

@DisplayName("비지니스 로직 - 페이지네이션")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.NONE, classes = PaginationService.class)
class PaginationServiceTest {

    private final PaginationService sut;

    public PaginationServiceTest(@Autowired PaginationService paginationService) {
        this.sut = paginationService;
    }

    @DisplayName("현재 페이지 번호와 총 페이지 수를 주면 페이징 바 리스트를 만들어준다.")
    @ParameterizedTest(name = "[{index}] {0}, {1} -> {2}") // 값을 여러번 테스트할 때 사용한다.
    @MethodSource
    void givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnPaginationBarNumber(int currentPageNum, int totalPages, List<Integer> expected) {
        // given


        // when
        List<Integer> actual = sut.getPaginationBarNumbers(currentPageNum, totalPages);


        // then
        assertThat(actual).isEqualTo(expected);

    }

    static Stream<Arguments> givenCurrentPageNumberAndTotalPages_whenCalculating_thenReturnPaginationBarNumber() {
        return Stream.of(
                arguments(0, 13, List.of(0, 1, 2, 3, 4)),
                arguments(1, 13, List.of(0, 1, 2, 3, 4)),
                arguments(2, 13, List.of(0, 1, 2, 3, 4)),
                arguments(3, 13, List.of(1, 2, 3, 4, 5)),
                arguments(4, 13, List.of(2, 3, 4, 5, 6)),
                arguments(5, 13, List.of(3, 4, 5, 6, 7)),
                arguments(6, 13, List.of(4, 5, 6, 7, 8)),
                arguments(10, 13, List.of(8, 9, 10, 11, 12)),
                arguments(11, 13, List.of(9, 10, 11, 12)),
                arguments(12, 13, List.of(10, 11, 12))
        );
    }

    @Test
    void givenNothing_whenCalling_thenReturnCurrentBarLength() {
        // given

        // when
        int barLength = sut.currentBarLength();


        // then
        assertThat(barLength).isEqualTo(5);
    }

}