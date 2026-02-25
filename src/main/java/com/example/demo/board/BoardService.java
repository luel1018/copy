package com.example.demo.board;

import com.example.demo.board.model.Board;
import com.example.demo.common.exception.BaseException;
import com.example.demo.user.UserRepository;
import com.example.demo.user.model.User;
import lombok.RequiredArgsConstructor;
import com.example.demo.board.model.BoardDto;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.List;

import static com.example.demo.common.model.BaseResponseStatus.FAIL;

@RequiredArgsConstructor
@Service
public class BoardService {
    private final BoardRepository boardRepository;
    private final UserRepository userRepository;

    private User getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || authentication.getName() == null) {
            throw BaseException.from(FAIL);
        }

        return userRepository.findByEmail(authentication.getName()).orElseThrow(
                () -> BaseException.from(FAIL)
        );
    }

    public BoardDto.RegRes register(BoardDto.RegReq dto) {
        User user = getLoginUser();

        Board entity = dto.toEntity();
        entity.setUser(user);

        boardRepository.save(entity);

        return BoardDto.RegRes.from(entity);
    }

    public List<BoardDto.ListRes> list() {
        List<Board> boardList = boardRepository.findAll();
        return boardList.stream().map(BoardDto.ListRes::from).toList();
    }

    public BoardDto.ReadRes read(Long idx) {
        Board board = boardRepository.findById(idx).orElseThrow();
        return BoardDto.ReadRes.from(board);
    }

    public BoardDto.RegRes update(Long idx, BoardDto.RegReq dto) {
        Board board = boardRepository.findById(idx).orElseThrow();
        board.update(dto);

        boardRepository.save(board);

        return BoardDto.RegRes.from(board);
    }

    public void delete(Long idx) {
        boardRepository.deleteById(idx);
    }
}