package lsdi.hyperledger.medicalRecord.assets;

import java.time.Instant;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public final class PrescricaoAsset {
    @Property
    public String idPrescricao;
    @Property
    public String idPaciente;
    @Property
    public String idMedico;
    @Property
    public String idMedicamento;
    @Property
    public int dosagem;
    @Property
    public String unidadeDosagem;
    @Property
    public String periodicidade;
    @Property
    public String dataHoraPrescricao;
/*    @Property
    public String viaAdministracao;
    @Property
    public String duracaoTratamento;
    @Property
    public String observacoesPrescricao;
    @Property
    public String statusPrescricao;*/
}
