package com.raf.restdemo.service.impl;

import com.raf.restdemo.domain.Product;
import com.raf.restdemo.dto.CommentDto;
import com.raf.restdemo.mapper.CommentMapper;
import com.raf.restdemo.repository.CommentRepository;
import com.raf.restdemo.repository.ProductRepository;
import com.raf.restdemo.service.CommentService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@Transactional
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;
    private final ProductRepository productRepository;
    private final CommentMapper commentMapper;

    @Override
    public Page<CommentDto> findAllByProductId(Long productId, Pageable pageable) {
        log.info("Getting all comments for product with id: {}. Pageable: {}.", productId, pageable);
        return commentRepository.findAllByProduct_Id(productId, pageable)
                .map(commentMapper::commentToCommentDto);
    }

    @Override
    public CommentDto addCommentOnProduct(Long productId, CommentDto commentDto) {
        log.info("Adding comment: {} on product with id: {}.", commentDto, productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new RuntimeException("Project not found"));

        return commentMapper.commentToCommentDto(commentRepository
                .save(commentMapper.commentDtoToComment(commentDto, product)));
    }

}
