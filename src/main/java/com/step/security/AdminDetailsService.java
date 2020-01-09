package com.step.security;

import com.step.entity.secondary.Admin;
import com.step.entity.secondary.Role;
import com.step.repository.secondary.LoginUserRepository;
import com.step.repository.secondary.RoleRepository;
import com.step.utils.tools.AesUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.common.exceptions.InvalidGrantException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Created by zhushubin  on 2019-08-28.
 * email:604580436@qq.com
 */
@Service
@Slf4j
public class AdminDetailsService implements UserDetailsService {
    @Autowired
    private BCryptPasswordEncoder passwordEncoder;
    @Autowired
    private LoginUserRepository loginUserRepository;
    @Autowired
    private RoleRepository roleRepository;

    @Override
    @Transactional(value = "transactionManagerSecondary", readOnly = true)
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // 这里的user只要是UserDetails的实现类即可
        Admin admin = loginUserRepository.findByUsername(username);
        if (admin == null) {
            throw new InvalidGrantException("用户名不存在");
        }
        if (admin.getIsLocked()) {
            throw new InvalidGrantException("用户名已被锁定");
        }
        admin.getRoles();
        try {
            String bp = AesUtils.encrypt(admin.getEncodedPassword(), "1234567890ABCDEF1234567890ABCDEf");
            String password = passwordEncoder.encode(bp);
            List<Role> roles= roleRepository.findAllByAdminId(admin.getId());
            List<GrantedAuthority> authorities = roles.stream().map(e -> {
                return new SimpleGrantedAuthority(e.getCode());
            }).collect(Collectors.toList());
            return new User(username,
                    password,
                    true,
                    true,
                    true,
                    true,
                    authorities);
        } catch (Exception e) {
            e.printStackTrace();
            log.error(e.getMessage());
        }

        return null;
    }

}
