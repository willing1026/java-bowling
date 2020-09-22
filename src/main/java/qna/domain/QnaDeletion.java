package qna.domain;

import qna.CannotDeleteException;

// 역할 - 질문을 삭제한다.
// 책임 - 삭제가능 여부 확인, 질문(답변) 삭제, 삭제이력 제공
public class QnaDeletion {
    private Question question;

    public QnaDeletion(Question question, User loginUser) throws CannotDeleteException {
        validate(question, loginUser);
        this.question = question;
    }

    private void validate(Question question, User loginUser) throws CannotDeleteException {
        if (!question.isOwner(loginUser)) {
            throw new CannotDeleteException("질문을 삭제할 권한이 없습니다.");
        }

        if (question.isExistOtherUserAnswer(loginUser)) {
            throw new CannotDeleteException("다른 사람이 쓴 답변이 있어 삭제할 수 없습니다.");
        }
    }

    //질문, 답변 삭제
    public Question delete() {
        return question.delete();
    }
}
