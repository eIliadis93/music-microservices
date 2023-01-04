package com.music.microservices.model;

import brave.internal.Nullable;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name="Users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @Column(length = 100)
    private String userName;
    private String password;
    private String role;
    private String token;
    @OneToMany(cascade = CascadeType.ALL)
    @Nullable
    private List<Song> songsList;
}
