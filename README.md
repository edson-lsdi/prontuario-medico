# Prontuário médico

Implementação de um prontuário médico com controle de acesso utilizando Hyperledger Fabric 2.5.10.

## Tecnologias
- Hyperledger fabric 2.5.10

## Como executar

### Iniciando os containers e criando o canal
```
./network.sh up createChannel -ca -s couchdb
```

### Executando o contrato no canal
```
./network.sh deployCC -ccn medical -ccp caminho_ate_o_contrato// -ccl java
```

## Criando credenciais

- Execute os comandos para gerar o material criptográfico dentro do container docker da `ca_org1`

```
docker cp ./scripts/createIdentities.sh ca_org1:/
docker exec -it ca_org1 bash /createIdentities.sh
```

- Caminho das credencias localmente
```
test-network/organizations/fabric-ca/org1/fabric-ca/clients
```
