package com.klapertart.appversioning;

import com.klapertart.sample.library.utils.Const;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

/**
 * @author tritronik
 * @since 7/10/2024
 */

@SpringBootTest
@Slf4j
class PrintLibraryConstTest {

    @Test
    void printConst() {
        Assertions.assertEquals("versi 002", Const.CONST_VERSI_002);
    }
}