package com.klapertart.appversioning;

import com.klapertart.sample.library.utils.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;

/**
 * @author tritronik
 * @since 7/10/2024
 */

@Component
@Slf4j
public class PrintLibraryConst {
    @PostConstruct
    public void printConst(){
        log.info("CONST VERSI 2: ", Const.CONST_VERSI_002);
    }
}
