package lsdi.hyperledger.medicalRecord.contract;

import com.owlike.genson.Genson;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.*;

import lsdi.hyperledger.medicalRecord.contract.prontuarioMedicoServices.EnfermeiroService;
import lsdi.hyperledger.medicalRecord.contract.prontuarioMedicoServices.PacienteService;
import lsdi.hyperledger.medicalRecord.contract.prontuarioMedicoServices.MedicoService;

@Contract(
        name = "medical_record",
        info = @Info(
                title = "medical-record",
                description = "Prontuário eletrônico",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ))
@Default
public class ProntuarioMedicoContract implements ContractInterface {
    private final Genson genson = new Genson();

    private final MedicoService medicoService = new MedicoService(genson);
    private final EnfermeiroService enfermeiroService = new EnfermeiroService(genson);
    private final PacienteService pacienteService = new PacienteService(genson);

    // Métodos de acesso do médico
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String adicionaEvolucao(Context ctx, String evolucaoJSON) {
        return medicoService.adicionaEvolucao(ctx, evolucaoJSON);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String adicionaPrescricao(Context ctx, String prescricaoJSON) {
        return medicoService.adicionaPrescricao(ctx, prescricaoJSON);
    }

    // Métodos de acesso do enfermeiro
    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String ministraMedicamento(Context ctx, String ministracaoJSON) {
        return enfermeiroService.ministraMedicamento(ctx, ministracaoJSON);
    }

    // Todos os papeis
    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaEvolucaoPaciente(Context ctx, String idPaciente) {
        return pacienteService.listaEvolucaoPaciente(ctx, idPaciente);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaMinistracaoMedicamentosPaciente(Context ctx, String idPaciente) {
        return pacienteService.listaMinistracaoMedicamentosPaciente(ctx, idPaciente);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaPrescricaoPaciente(Context ctx, String idPaciente) {
        return pacienteService.listaPrescricaoPaciente(ctx, idPaciente);
    }
}
