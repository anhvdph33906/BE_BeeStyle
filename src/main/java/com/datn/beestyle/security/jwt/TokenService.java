package com.datn.beestyle.security.jwt;

import com.datn.beestyle.exception.ResourceNotFoundException;
import com.datn.beestyle.repository.TokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TokenService {

//    private final TokenRepository tokenRepository;

//    public int save(Token token) {
//        Optional<Token> tokenOptional = tokenRepository.findByUsername(token.getUsername());
//        if (tokenOptional.isEmpty()) {
//            return tokenRepository.save(token).getId();
//        }
//        Token currentToken = tokenOptional.get();
//        currentToken.setAccessToken(token.getAccessToken());
//        currentToken.setRefreshToken(token.getRefreshToken());
//        return tokenRepository.save(currentToken).getId();
//    }
//
//    public String delete(Token token) {
//        tokenRepository.delete(token);
//        return "Deleted";
//    }
//
//    public Token getByUsername(String username) {
//        return tokenRepository.findByUsername(username)
//                .orElseThrow(() -> new ResourceNotFoundException("Token not exists"));
//    }
}
