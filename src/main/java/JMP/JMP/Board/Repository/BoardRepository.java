package JMP.JMP.Board.Repository;

import JMP.JMP.Board.Entity.Board;
import JMP.JMP.Enum.BoardType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BoardRepository extends JpaRepository<Board, Long> {

    Page<Board> findByBoardType(BoardType boardType, Pageable pageable);

    List<Board> findAllByWriter_IdAndBoardType(Long writerId, BoardType boardType);
}
