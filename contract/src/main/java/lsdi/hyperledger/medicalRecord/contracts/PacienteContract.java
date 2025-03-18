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
import org.hyperledger.fabric.shim.ChaincodeException;
import org.hyperledger.fabric.shim.ChaincodeStub;
import org.hyperledger.fabric.shim.ledger.KeyValue;
import org.hyperledger.fabric.shim.ledger.QueryResultsIterator;

import com.owlike.genson.Genson;

import lsdi.hyperledger.medicalRecord.assets.EvolucaoAsset;
import lsdi.hyperledger.medicalRecord.assets.MinistracaoMedicamentoAsset;

import lsdi.hyperledger.medicalRecord.exceptions.AssetException.*;
import lsdi.hyperledger.medicalRecord.exceptions.CertificateException.*;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Contract(
        name = "paciente",
        info = @Info(
                title = "paciente",
                description = "Ações que um paciente em um prontuário eletrônico",
                version = "0.0.1-SNAPSHOT",
                license = @License(
                        name = "Apache 2.0 License",
                        url = "http://www.apache.org/licenses/LICENSE-2.0.html")
        ))

@Default
public final class PacienteContract implements ContractInterface {

    private final Genson genson = new Genson();

    private String getClientId (Context ctx) {
        String clientCertificate = ctx.getClientIdentity().getId();

        Pattern pattern = Pattern.compile("CN=([^,\\s]+)");
        Matcher matcher = pattern.matcher(clientCertificate);

        if (matcher.find()) {
            return matcher.group(1); // Retorna o CN id do certificado
        }

        String errorMessage = String.format("Id not found");
        throw new ChaincodeException(errorMessage, CertificateErrors.ID_NOT_FOUND.toString());
    }

    private boolean hasClientAcess(Context ctx, String idPaciente) {
        var clientId = getClientId(ctx);

        if (clientId.equals(idPaciente)) {
            return true;
        }

        String errorMessage = String.format("Client don't have acess");
        throw new ChaincodeException(errorMessage, AssetTransferErrors.ACCESS_DENIED.toString());
    }

    @Transaction(intent = Transaction.TYPE.EVALUATE)
    public String listaEvolucaoPaciente(Context ctx, String idPaciente) {
        ChaincodeStub stub = ctx.getStub();

        hasClientAcess(ctx, idPaciente);

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

        hasClientAcess(ctx, idPaciente);

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
