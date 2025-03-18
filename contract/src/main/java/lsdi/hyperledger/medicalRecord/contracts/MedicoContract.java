/*
 * SPDX-License-Identifier: Apache-2.0
 */

package lsdi.hyperledger.medicalRecord;

import lsdi.hyperledger.medicalRecord.contracts.Utils;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

import lsdi.hyperledger.medicalRecord.assets.EvolucaoAsset;
import lsdi.hyperledger.medicalRecord.assets.MinistracaoMedicamentoAsset;
import lsdi.hyperledger.medicalRecord.assets.PrescricaoAsset;

import lsdi.hyperledger.medicalRecord.exceptions.AssetException.AssetTransferErrors;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "medico",
        info = @Info(
                title = "medico",
                description = "Ações de um médico em um prontuário eletrônico",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ))

@Default
public final class MedicoContract implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void adicionaEvolucao(Context ctx, String evolucaoJSON) {
        EvolucaoAsset evolucao = genson.deserialize(evolucaoJSON, EvolucaoAsset.class);
        String evolucaoKey = "EVOLUCAO_" + evolucao.idEvolucao;

        if (Utils.AssetExists(ctx, evolucaoKey)) {
            String errorMessage = String.format("Evolucao %s already exists", evolucaoKey);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }
        ctx.getStub().putStringState(evolucaoKey, genson.serialize(evolucao));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void adicionaPrescricao(Context ctx, String prescricaoJSON) {
        PrescricaoAsset prescricao = genson.deserialize(prescricaoJSON, PrescricaoAsset.class);
        String prescricaoKey = "PRESCRICAO_" + prescricao.idPrescricao;

        if (Utils.AssetExists(ctx, prescricaoKey)) {
            String errorMessage = String.format("Evolucao %s already exists", prescricaoKey);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }
        ctx.getStub().putStringState(prescricaoKey, genson.serialize(prescricao));
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaEvolucaoPaciente(Context ctx, String idPaciente) {
        ChaincodeStub stub = ctx.getStub();

        String queryString = String.format("{\"selector\":{\"idPaciente\":\"%s\"}}", idPaciente);
        QueryResultsIterator<KeyValue> resultados = stub.getQueryResult(queryString);

        List<EvolucaoAsset> queryResults = new ArrayList<>();
        Genson genson = new Genson();

        for (KeyValue result : resultados) {
            EvolucaoAsset evolucao = genson.deserialize(result.getStringValue(), EvolucaoAsset.class);
             queryResults.add(evolucao);
        }

        return genson.serialize(queryResults);
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaMinistracaoMedicamentosPaciente(Context ctx, String idPaciente) {
        ChaincodeStub stub = ctx.getStub();

        String queryString = String.format("{\"selector\":{\"idPaciente\":\"%s\"}}", idPaciente);
        QueryResultsIterator<KeyValue> resultados = stub.getQueryResult(queryString);

        List<MinistracaoMedicamentoAsset> ministracoes = new ArrayList<>();
        Genson genson = new Genson();

        for (KeyValue result : resultados) {
            MinistracaoMedicamentoAsset ministracao = genson.deserialize(result.getStringValue(), MinistracaoMedicamentoAsset.class);
            ministracoes.add(ministracao);
        }

        return genson.serialize(ministracoes);
    }
}
