package lsdi.hyperledger.medicalRecord.contracts.medicalRecordServices;

import java.util.ArrayList;
import java.util.List;

import com.owlike.genson.Genson;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Transaction;

import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import lsdi.hyperledger.medicalRecord.assets.EvolucaoAsset;
import lsdi.hyperledger.medicalRecord.assets.MinistracaoMedicamentoAsset;
import lsdi.hyperledger.medicalRecord.assets.PrescricaoAsset;

import lsdi.hyperledger.medicalRecord.contracts.ContractServiceAbstract;

public class MedicalRecordService extends ContractServiceAbstract {
    private final String[] rolesFullAccess = {"medico", "enfermeiro"};
    private final String[] rolesRestrictedAccessById = {"paciente"};

    public MedicalRecordService(Genson genson) {
        super(genson);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaEvolucaoPaciente(Context ctx, String idPaciente) {
        ChaincodeStub stub = ctx.getStub();

        hasRoleAccessById(ctx, idPaciente, rolesFullAccess, rolesRestrictedAccessById);

        String queryString = String.format("{\"selector\":{\"idPaciente\":\"%s\"}}", idPaciente);
        QueryResultsIterator<KeyValue> resultados = stub.getQueryResult(queryString);

        List<EvolucaoAsset> queryResults = new ArrayList<>();

        for (KeyValue result : resultados) {
            if (result.getKey().startsWith("EVOLUCAO_")) {
                EvolucaoAsset evolucao = genson.deserialize(result.getStringValue(), EvolucaoAsset.class);
                queryResults.add(evolucao);
            }
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaMinistracaoMedicamentosPaciente(Context ctx, String idPaciente) {
        ChaincodeStub stub = ctx.getStub();

        hasRoleAccessById(ctx, idPaciente, rolesFullAccess, rolesRestrictedAccessById);

        String queryString = String.format("{\"selector\":{\"idPaciente\":\"%s\"}}", idPaciente);
        QueryResultsIterator<KeyValue> resultados = stub.getQueryResult(queryString);

        List<MinistracaoMedicamentoAsset> ministracoes = new ArrayList<>();

        for (KeyValue result : resultados) {
            if (result.getKey().startsWith("MINISTRACAO_")) {
                MinistracaoMedicamentoAsset ministracao = genson.deserialize(result.getStringValue(), MinistracaoMedicamentoAsset.class);
                ministracoes.add(ministracao);
            }
        }

        return genson.serialize(ministracoes);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaPrescricaoPaciente(Context ctx, String idPaciente) {
        ChaincodeStub stub = ctx.getStub();

        hasRoleAccessById(ctx, idPaciente, rolesFullAccess, rolesRestrictedAccessById);

        String queryString = String.format("{\"selector\":{\"idPaciente\":\"%s\"}}", idPaciente);
        QueryResultsIterator<KeyValue> resultados = stub.getQueryResult(queryString);

        List<PrescricaoAsset> queryResults = new ArrayList<>();

        for (KeyValue result : resultados) {
            if (result.getKey().startsWith("PRESCRICAO_")) {
                PrescricaoAsset prescricao = genson.deserialize(result.getStringValue(), PrescricaoAsset.class);
                queryResults.add(prescricao);
            }
        }

        return genson.serialize(queryResults);
    }
}
