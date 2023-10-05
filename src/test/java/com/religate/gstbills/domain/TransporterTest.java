package com.religate.gstbills.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.religate.gstbills.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TransporterTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Transporter.class);
        Transporter transporter1 = new Transporter();
        transporter1.setId(1L);
        Transporter transporter2 = new Transporter();
        transporter2.setId(transporter1.getId());
        assertThat(transporter1).isEqualTo(transporter2);
        transporter2.setId(2L);
        assertThat(transporter1).isNotEqualTo(transporter2);
        transporter1.setId(null);
        assertThat(transporter1).isNotEqualTo(transporter2);
    }
}
