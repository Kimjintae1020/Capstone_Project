package JMP.JMP.Comment.Repository;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Comment.Entity.Comment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

    List<Comment> findAllByBoard_BoardId(Long boardId);


    int countByBoard(Board board);
}
