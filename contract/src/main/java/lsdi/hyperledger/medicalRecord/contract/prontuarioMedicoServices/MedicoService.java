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

import lsdi.hyperledger.medicalRecord.assets.EvolucaoAsset;
import lsdi.hyperledger.medicalRecord.assets.PrescricaoAsset;

import lsdi.hyperledger.medicalRecord.contract.MonitorControleAcessoAbstract;

public final class MedicoService extends MonitorControleAcessoAbstract {

    public MedicoService(Genson genson) {
        super(genson);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String adicionaEvolucao(Context ctx, String evolucaoJSON) {
        usuarioTemPapel(ctx, "medico");

        EvolucaoAsset evolucao = genson.deserialize(evolucaoJSON, EvolucaoAsset.class);
        String evolucaoKey = "EVOLUCAO_" + evolucao.idEvolucao;

        if (Utils.AssetExists(ctx, evolucaoKey)) {
            String errorMessage = String.format("Evolucao %s already exists", evolucaoKey);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }
        ctx.getStub().putStringState(evolucaoKey, genson.serialize(evolucao));

        return genson.serialize(evolucaoKey);
    }

    @Transaction(intent = Transaction.TYPE.SUBMIT)
    public String adicionaPrescricao(Context ctx, String prescricaoJSON) {
        usuarioTemPapel(ctx, "medico");

        PrescricaoAsset prescricao = genson.deserialize(prescricaoJSON, PrescricaoAsset.class);
        String prescricaoKey = "PRESCRICAO_" + prescricao.idPrescricao;

        if (Utils.AssetExists(ctx, prescricaoKey)) {
            String errorMessage = String.format("Prescricao  %s already exists", prescricaoKey);
            throw new ChaincodeException(errorMessage, AssetTransferErrors.ASSET_ALREADY_EXISTS.toString());
        }
        ctx.getStub().putStringState(prescricaoKey, genson.serialize(prescricao));

        return genson.serialize(prescricaoKey);
    }
}
