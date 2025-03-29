/*
 * SPDX-License-Identifier: Apache-2.0
 */

package lsdi.hyperledger.medicalRecord.contract.prontuarioMedicoServices;

import com.owlike.genson.Genson;

import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.contract.annotation.Transaction;

import org.hyperledger.fabric.shim.ChaincodeException;
import lsdi.hyperledger.medicalRecord.utils.Utils;
import lsdi.hyperledger.medicalRecord.exceptions.AssetException.AssetTransferErrors;

import lsdi.hyperledger.medicalRecord.contract.MonitorControleAcessoAbstract;

import lsdi.hyperledger.medicalRecord.assets.MinistracaoMedicamentoAsset;

public final class EnfermeiroService extends MonitorControleAcessoAbstract {

    public EnfermeiroService(Genson genson) {
        super(genson);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String ministraMedicamento(Context ctx, String ministracaoJSON) {
        usuarioTemPapel(ctx, "enfermeiro");

        MinistracaoMedicamentoAsset ministracao = genson.deserialize(ministracaoJSON, MinistracaoMedicamentoAsset.class);
        String ministracaoKey = "MINISTRACAO_" + ministracao.idMinistracao;

        if (Utils.AssetExists(ctx, ministracaoKey)) {
            String errorMessage = String.format("Ministracao %s already exists", ministracaoKey);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }
        ctx.getStub().putStringState(ministracaoKey, genson.serialize(ministracao));

        return genson.serialize(ministracaoKey);
    }
}
