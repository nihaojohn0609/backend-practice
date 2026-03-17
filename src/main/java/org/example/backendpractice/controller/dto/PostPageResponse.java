package org.example.backendpractice.controller.dto;

import lombok.Builder;
import lombok.Data;
import org.example.backendpractice.entity.Post;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@Builder
public class PostPageResponse {

    private List<PostResponse> posts;
    private int currentPage;
    private int totalPages;
    private long totalElements;
    private boolean hasNext;
    private boolean hasPrevious;

    public static PostPageResponse from(Page<Post> page) {
        return PostPageResponse.builder()
                .posts(page.getContent().stream().map(PostResponse::from).toList())
                .currentPage(page.getNumber())
                .totalPages(page.getTotalPages())
                .totalElements(page.getTotalElements())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
