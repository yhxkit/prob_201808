package com.prob.pilot18091701.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;

@Getter
@Setter
@Entity

public class Comment {


    @Id
    @GeneratedValue
    private int commentIdx;

    private String content;

    private LocalDateTime commentTime;

    @ManyToOne
    @JoinColumn(name = "belonging_post_idx")
    private Post belongingPost;


}
