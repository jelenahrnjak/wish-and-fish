package com.example.WishAndFish.service;

import com.example.WishAndFish.dto.CommentDTO;
import com.example.WishAndFish.model.Comment;
import com.example.WishAndFish.model.Reservation;
import com.example.WishAndFish.repository.ClientRepository;
import com.example.WishAndFish.repository.CommentRepository;
import com.example.WishAndFish.repository.ReservationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CommentService {
    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ClientRepository clientRepository;

    @Autowired
    ReservationRepository reservationRepository;

    public Comment addCommentToClient(CommentDTO comment){
        Comment c = new Comment();
        c.setContent(comment.getContent());
        c.setCame(comment.getCame());
        c.setBadComment(comment.getBad());
        c.setClient(clientRepository.findByEmail(comment.getClient()));
        commentRepository.save(c);

        Reservation r = this.reservationRepository.findById(comment.getReservationID()).orElse(null);
        r.setCommented(true);
        reservationRepository.save(r);
        return c;
    }
}