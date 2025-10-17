package com.javarush.kazakov.entity.quest;


import com.javarush.kazakov.entity.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "quest", schema = "quests")
public class Quest implements QuestItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;
    private String title;
    @OneToOne(cascade = CascadeType.ALL)
    @JoinTable(schema = "quests", name = "quest_first_question",
            joinColumns = {@JoinColumn(name = "quest_id", referencedColumnName = "id")},
            inverseJoinColumns = {@JoinColumn(name = "question_id", referencedColumnName = "id")})
    private Question firstQuestion;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "author_id", referencedColumnName = "id")
    @OnDelete(action = OnDeleteAction.SET_NULL)
    User author;
}
