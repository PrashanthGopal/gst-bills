package com.religate.gstbills.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.religate.gstbills.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class OrgUserRoleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(OrgUserRole.class);
        OrgUserRole orgUserRole1 = new OrgUserRole();
        orgUserRole1.setId(1L);
        OrgUserRole orgUserRole2 = new OrgUserRole();
        orgUserRole2.setId(orgUserRole1.getId());
        assertThat(orgUserRole1).isEqualTo(orgUserRole2);
        orgUserRole2.setId(2L);
        assertThat(orgUserRole1).isNotEqualTo(orgUserRole2);
        orgUserRole1.setId(null);
        assertThat(orgUserRole1).isNotEqualTo(orgUserRole2);
    }
}
