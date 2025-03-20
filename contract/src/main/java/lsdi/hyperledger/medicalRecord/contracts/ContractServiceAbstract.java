package lsdi.hyperledger.medicalRecord.contracts;

import java.util.Arrays;

import com.owlike.genson.Genson;

import lsdi.hyperledger.medicalRecord.exceptions.AssetException;
import org.hyperledger.fabric.contract.Context;
import org.hyperledger.fabric.shim.ChaincodeException;

import static lsdi.hyperledger.medicalRecord.utils.Utils.hasClientSameId;

public abstract class ContractServiceAbstract {
    protected final Genson genson;
    
    protected ContractServiceAbstract(Genson genson) {
        this.genson = genson;
    }

    private String[] getRoles(Context ctx) {
        return (ctx.getClientIdentity().getAttributeValue("role")).split(";");
    }

    private boolean hasRole(Context ctx, String role) {
        String[] roles = getRoles(ctx);
        return Arrays.asList(roles).contains(role);
    }

    public boolean hasRoleAccess(Context ctx, String role) {
        if (hasRole(ctx, role)) {
            return true;
        }

        String errorMessage = String.format("Client don't have acess");
        throw new ChaincodeException(errorMessage, AssetException.AssetTransferErrors.ACCESS_DENIED.toString());
    }

    public boolean hasRoleAccessById(Context ctx, String id, String[] rolesRestrictedAccessWithSameId) {

        for (int i = 0; i < rolesRestrictedAccessWithSameId.length; i++) {
            // Verify if identity contains role with need contain that same id to access
            if (hasRole(ctx, rolesRestrictedAccessWithSameId[i])) {
                return hasClientSameId(ctx, id);
            }
        }
        return true;
    }
}
