package com.home.test180924.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity
public class Comment implements Article{


    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int commentIdx;

    private String commentContent;

    @JsonFormat(pattern = "yyyy-MM-dd kk:mm:ss")
    @CreationTimestamp
    private LocalDateTime commentTime;

    @ManyToOne
    @JoinColumn(name = "belonging_post_idx")
    private Post belongingPost; // 프론트에서 파라미터로 받을때 대소문자 구분해서 보내야 제대로 커맨드 객체에 매핑됩니다

    @ManyToOne
    @JoinColumn(name = "comment_writer_email")
    private Account commentWriter;


}
