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

        throw new ChaincodeException(AssetException.AssetTransferErrors.ACCESS_DENIED.name());
    }

    public boolean hasRoleAccessById(Context ctx, String id, String [] rolesFullAccess, String[] rolesRestrictedAccessById) {
        String[] roles = getRoles(ctx);

        // Verify if identity have role with full access a method
        for (String role : roles) {
            if (Arrays.asList(rolesFullAccess).contains(role)) {
                return true;
            }
        }

        // Verify if identity have role restricted access only that same id method
        for (String role : roles) {
            if (Arrays.asList(rolesRestrictedAccessById).contains(role)) {
                return hasClientSameId(ctx, id);
            }
        }
        throw new ChaincodeException(AssetException.AssetTransferErrors.ACCESS_DENIED.name());
    }
}
