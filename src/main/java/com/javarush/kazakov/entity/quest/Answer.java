package com.javarush.kazakov.entity.quest;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "answer", schema = "quests")
public class Answer implements QuestItem{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinTable(schema = "quests", name = "answer_next_question",
            joinColumns = {@JoinColumn(name = "answer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")})
    private Question question;
    @OneToOne(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinTable(schema = "quests", name = "answer_result",
            joinColumns = {@JoinColumn(name = "answer_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "result_id", referencedColumnName = "id")})
    private Result result;
}
