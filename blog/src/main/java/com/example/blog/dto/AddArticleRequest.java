package com.example.blog.dto;

import com.example.blog.domain.Article;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class AddArticleRequest {
    private String title;
    private String content;

    public Article toEntity() { // DTO를 엔티티로 만들어준다.(빌더패턴으로 객체 생성) 블로그 글 추가할 때 저장할 엔티티로 변환하는 용도로 사용. 그냥 객체 없으니까 Article 객체 하나 생성해준다고 생각하면 됩니다. toEntity()대신 create()라고 하던가. 헷갈리면.
        return Article.builder()
                .title(title)
                .content(content)
                .build();
    }
}
