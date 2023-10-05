package com.religate.gstbills.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.religate.gstbills.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StateCodeTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StateCode.class);
        StateCode stateCode1 = new StateCode();
        stateCode1.setId(1L);
        StateCode stateCode2 = new StateCode();
        stateCode2.setId(stateCode1.getId());
        assertThat(stateCode1).isEqualTo(stateCode2);
        stateCode2.setId(2L);
        assertThat(stateCode1).isNotEqualTo(stateCode2);
        stateCode1.setId(null);
        assertThat(stateCode1).isNotEqualTo(stateCode2);
    }
}
