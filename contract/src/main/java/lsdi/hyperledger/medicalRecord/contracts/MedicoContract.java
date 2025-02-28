/*
 * SPDX-License-Identifier: Apache-2.0
 */

package lsdi.hyperledger.medicalRecord;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.ContractInterface;
import org.hyperledger.fabric.contract.annotation.Contract;
import org.hyperledger.fabric.contract.annotation.Default;
import org.hyperledger.fabric.contract.annotation.Info;
import org.hyperledger.fabric.contract.annotation.License;
import org.hyperledger.fabric.contract.annotation.Transaction;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

import lsdi.hyperledger.medicalRecord.assets.EvolucaoAsset;
import lsdi.hyperledger.medicalRecord.assets.MinistracaoMedicamentoAssset;
import lsdi.hyperledger.medicalRecord.assets.PrescricaoAsset;

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

    private enum AssetTransferErrors {
        ASSET_NOT_FOUND,
        ASSET_ALREADY_EXISTS
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void adicionaEvolucao(Context ctx, String evolucaoJSON) {
        EvolucaoAsset evolucao = genson.deserialize(evolucaoJSON, EvolucaoAsset.class);
        String evolucaoKey = "EVOLUCAO_" + evolucao.idEvolucao;
        ctx.getStub().putStringState(evolucaoKey, genson.serialize(evolucao));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void adicionaPrescricao(Context ctx, String prescricaoJSON) {
        PrescricaoAsset prescricao = genson.deserialize(prescricaoJSON, PrescricaoAsset.class);
        String prescricaoKey = "PRESCRICAO_" + prescricao.idPrescricao;
        ctx.getStub().putStringState(prescricaoKey, genson.serialize(prescricao));
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void ministraMedicamento(Context ctx, String ministracaoJSON) {
        MinistracaoMedicamentoAssset ministracao = genson.deserialize(ministracaoJSON, MinistracaoMedicamentoAssset.class);
        String ministracaoKey = "MINISTRACAO_" + ministracao.idMinistracao;
        ctx.getStub().putStringState(ministracaoKey, genson.serialize(ministracao));
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

        List<MinistracaoMedicamentoAssset> ministracoes = new ArrayList<>();
        Genson genson = new Genson();

        for (KeyValue result : resultados) {
            MinistracaoMedicamentoAssset ministracao = genson.deserialize(result.getStringValue(), MinistracaoMedicamentoAssset.class);
            ministracoes.add(ministracao);
        }

        return genson.serialize(ministracoes);
    }
}
