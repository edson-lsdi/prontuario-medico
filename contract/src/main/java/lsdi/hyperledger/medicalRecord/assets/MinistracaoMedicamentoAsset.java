package lsdi.hyperledger.medicalRecord.assets;

import java.time.Instant;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public final class MinistracaoMedicamentoAsset {
    @Property
    public String idMinistracao;
    @Property
    public String idPrescricao;
    @Property
    public String idPaciente;
    @Property
    public String idEnfermeiro;
    @Property
    public String idMedicamento;
    @Property
    public int dosagem;
    @Property
    public String unidadeDosagem;
    @Property
    public String dataHoraMinistracao;
/*    @Property
    public String reacaoAdversa;
    @Property
    public String observacoesEnfermeiro;
    @Property
    public String confirmacaoPaciente;*/
}
