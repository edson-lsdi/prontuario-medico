/*
 * SPDX-License-Identifier: Apache-2.0
 */

package lsdi.hyperledger.medicalRecord.contracts;

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

import lsdi.hyperledger.medicalRecord.exceptions.AssetException.AssetTransferErrors;
import lsdi.hyperledger.medicalRecord.utils.Utils;

import lsdi.hyperledger.medicalRecord.assets.EvolucaoAsset;
import lsdi.hyperledger.medicalRecord.assets.MinistracaoMedicamentoAsset;

import java.util.ArrayList;
import java.util.List;

@Contract(
        name = "enfermeiro",
        info = @Info(
                title = "enfermeiro",
                description = "Ações que um enfermeiro em um prontuário eletrônico",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ))

@Default
public final class EnfermeiroContract implements ContractInterface {

    private final Genson genson = new Genson();

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public void ministraMedicamento(Context ctx, String ministracaoJSON) {
        MinistracaoMedicamentoAsset ministracao = genson.deserialize(ministracaoJSON, MinistracaoMedicamentoAsset.class);
        String ministracaoKey = "MINISTRACAO_" + ministracao.idMinistracao;

        if (Utils.AssetExists(ctx, ministracaoKey)) {
            String errorMessage = String.format("Evolucao %s already exists", ministracaoKey);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }
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

        List<MinistracaoMedicamentoAsset> ministracoes = new ArrayList<>();
        Genson genson = new Genson();

        for (KeyValue result : resultados) {
            MinistracaoMedicamentoAsset ministracao = genson.deserialize(result.getStringValue(), MinistracaoMedicamentoAsset.class);
            ministracoes.add(ministracao);
        }

        return genson.serialize(ministracoes);
    }
}
