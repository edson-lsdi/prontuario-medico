package lsdi.hyperledger.medicalRecord.assets;

import java.time.Instant;

import org.hyperledger.fabric.contract.annotation.DataType;
import org.hyperledger.fabric.contract.annotation.Property;

@DataType()
public final class EvolucaoAsset {
    @Property
    public String idEvolucao;
    @Property
    public String idPaciente;
    @Property
    public String idMedico;
    @Property
    public String descricaoEvolucao;
    @Property
    public String dataHoraEvolucao;
    /*@Property
    public String observacoes;
    @Property
    public String status;*/
}