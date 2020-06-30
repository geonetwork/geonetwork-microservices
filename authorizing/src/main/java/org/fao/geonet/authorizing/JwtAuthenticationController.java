package org.fao.geonet.authorizing;


import org.fao.geonet.common.JwtTokenUtil;
import org.fao.geonet.domain.ReservedGroup;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashSet;
import java.util.Set;


@RestController
@CrossOrigin
public class JwtAuthenticationController {

    @Autowired
    private AuthenticationManager authenticationManager;
    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @RequestMapping(value = "/authenticate", method = RequestMethod.POST)
    public ResponseEntity<?> createAuthenticationToken(@RequestBody JwtRequest authenticationRequest) throws Exception {
        Authentication authentication = authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());
        String userName = ((User) authentication.getPrincipal()).getUsername();
        Set<Integer> editingGroup = new HashSet<>();
        editingGroup.add(ReservedGroup.all.getId());
        Set<Integer> viewingGroup = new HashSet<>();
        viewingGroup.add(ReservedGroup.all.getId());
        if ("momo".equalsIgnoreCase(userName)) {
            editingGroup.add(33);
            viewingGroup.add(33);
            viewingGroup.add(42);
        }
        final String token = jwtTokenUtil.generateToken((UserDetails) authentication.getPrincipal(), viewingGroup, editingGroup);
        return ResponseEntity.ok(new JwtResponse(token));
    }
    private Authentication authenticate(String username, String password) throws Exception {
        try {
            return authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
        } catch (DisabledException e) {
            throw new Exception("USER_DISABLED", e);
        } catch (BadCredentialsException e) {
            throw new Exception("INVALID_CREDENTIALS", e);
        }
    }
}

