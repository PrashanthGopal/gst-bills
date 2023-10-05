package com.religate.gstbills.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.religate.gstbills.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgUsersTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUsers.class);
        OrgUsers orgUsers1 = new OrgUsers();
        orgUsers1.setId(1L);
        OrgUsers orgUsers2 = new OrgUsers();
        orgUsers2.setId(orgUsers1.getId());
        assertThat(orgUsers1).isEqualTo(orgUsers2);
        orgUsers2.setId(2L);
        assertThat(orgUsers1).isNotEqualTo(orgUsers2);
        orgUsers1.setId(null);
        assertThat(orgUsers1).isNotEqualTo(orgUsers2);
    }
}
