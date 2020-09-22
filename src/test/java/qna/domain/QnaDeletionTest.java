package qna.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qna.CannotDeleteException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class QnaDeletionTest {

    public static final Question Q1 = new Question("title1", "contents1").writeBy(UserTest.JAVAJIGI);
    public static final Answer A1 = new Answer(UserTest.JAVAJIGI, QuestionTest.Q1, "Answers Contents1");
    public static final User JAVAJIGI = new User(1L, "javajigi", "password", "name", "javajigi@slipp.net");
    public static final User SANJIGI = new User(2L, "sanjigi", "password", "name", "sanjigi@slipp.net");

    @BeforeEach
    void setUp() {
        Q1.addAnswer(A1);
    }

    @DisplayName("질문삭제 객체 생성")
    @Test
    void newInstance() throws CannotDeleteException {
        //when
        QnaDeletion qnaDeletion = new QnaDeletion(Q1, JAVAJIGI);

        //then
        assertThat(qnaDeletion).isNotNull();
    }

    @DisplayName("질문삭제 객체 생성 실패 - 글쓴이와 다른 로그인 사용자")
    @Test
    void newInstanceWithWrongUser() {
        //when
        assertThatThrownBy(() -> new QnaDeletion(Q1, SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문삭제 객체 생성 실패 - 글쓴이와 다른 답변자가 있는 경우")
    @Test
    void newInstanceWithWrongAnswerUser() {
        //given
        Answer A2 = new Answer(UserTest.SANJIGI, QuestionTest.Q1, "Answers Contents2");
        Q1.addAnswer(A2);

        //when
        assertThatThrownBy(() -> new QnaDeletion(Q1, SANJIGI))
                .isInstanceOf(CannotDeleteException.class);
    }

    @DisplayName("질문, 답변 삭제")
    @Test
    void delete() throws Exception {
        //given
        QnaDeletion qnaDeletion = new QnaDeletion(Q1, JAVAJIGI);

        //when
        Question question = qnaDeletion.delete();

        //then
        assertThat(question.isDeleted()).isTrue();
    }

    @DisplayName("삭제이력 반환")
    @Test
    void getDeleteHistory() throws CannotDeleteException {
        //given
        QnaDeletion qnaDeletion = new QnaDeletion(Q1, JAVAJIGI);

        //when
        List<DeleteHistory> deleteHistories = qnaDeletion.getDeleteHistory();
    }
}
