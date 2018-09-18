package com.prob.pilot18091701.entity;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Entity
public class Post {

    @Id
    @GeneratedValue
    private int postIdx;

    private String title;
    private String content;

    private LocalDateTime postTime;


    @ManyToOne
    @JoinColumn(name = "writer_email")
    private Account writer;

    @OneToMany(mappedBy = "belongingPost")
    private List<Comment> comments;



}
