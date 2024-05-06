package ru.otus.hw.models;

import jakarta.persistence.*;
import lombok.*;

@NamedEntityGraph(name = "comment-with-book-graph",
        attributeNodes = @NamedAttributeNode("book"))
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "comments")
public class Comment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

}
