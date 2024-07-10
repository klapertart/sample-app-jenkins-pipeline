package com.klapertart.appversioning;

import com.klapertart.sample.library.utils.Const;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author tritronik
 * @since 7/10/2024
 */

@Component
@Slf4j
public class PrintLibraryConst {
    public void printConst(){
        log.info("CONST VERSI 2: ", Const.CONST_VERSI_002);
    }
}
