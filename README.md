# MI Concorrência e Conectividade: Transações Bancárias Distribuidas
O objetivo deste projeto foi a implementação de um sistema distribuído para transações bancárias e utilização de algoritmos para resolver o problema de concorrencia dos dados. 

## Introdução

## Fundamentação Teórica

## Metodologia
Para solucionar o problema proposto, primeiro foram iniciados os estudos para entender como funciona os algoritmos distribuídos que buscam resolvel os problemas de concorrencia. A principio foi estudado os algoritmos de Lamport, Lamport com multicast, Token Ring, Exclusão mutua e relogio vetorial. Após os estudos foi feito o desenvolvimento do mesmo e depois de funcionando foi iniciado o desenvolvimento do sistena proposto. Ao finalizar o desenvolvimento do sistema solicitado, foi pesquisado como criar uma imagem docker da aplicação desenvolvida e executá-la dentro de um container.

A linguagem de programação escolhida para a solução do problema foi Java com o uso do Spring Framework para construção das APIs Rest.

Foi utilizado como padrão para tráfego de dados o  formato JSON(Javascript Object Notation) para comunicação das apis.

Para testar as aplicações foi feito a utilização de conteiners docker e do postman para testar as APIs.
## Resultados
A solução do problema contou com 4 servidores de bancos distintos formando uma rede em anel. Após subir os servidores há um endpoint para incialização do algoritmo do token ring. Para tratar da concorrência nas requisições dentro do próprio servidor foi utilizado semaforo para cada conta. As transações então podem ocorrer sendo as requisições disparadas de modo simultâneo que já foi feito o tratamento de concorrência e caso há algum problema em uma requisição, as alterações feitas por ela até o momento do erro são desfeitas.

### Servidor do Banco
Foram desenvolvido 4 servidores de bancos, entretanto a parte lógica ficou duplicada, mudando apenas a lógica para construção da rede em anel. Através de cada servidor é possível realizar o cadastro de contas novas com a restrição de que o número da conta não pode ser repetido. É possível realizar saques, depositos e consultas nos servidores além da funcionalidade de transferências que permite que o cliente faça transações de n contas de origem para uma conta de destino, visto que a conta de destino pode estar em qualquer servidor e as contas de origem podem estar em qualquer servidor também contanto que o cliente tenha conta no servidor do banco no qual ele requisitou a transação. Foi feito também o tratamento de exceções e possíveis falhas no que tinge as transferências, então os casos de a conta de origem informada não existir, conta de destino não desistir, saldo insuficiente, foram todos validados e dão uma resposta bem detalhada a requisição da API. Quanto ao algoritmo de token ring ele não é tolerante a falhas, então caso o servidor de um dos bancos caia o sistema fica indisponível para as operações que editem o valor do saldo das contas, pois o token do algoritmo foi perdido já que a rede em anel foi desfeita.

### Docker
Foi criado uma imagem docker para cada aplicação apresentada acima e estas imagens foram disponibilizadas no repositório online do docker hub. Para poder ter estas aplicações executando em um computador basta alterar o nome da imagem para o nome correspondente no dockerhub no arquivo docker-compose.yml, após isso basta abrir no terminal a pasta raiz do sistema desejado e executar o seguinte comando:


```
$ docker compose up
```

É importante ressaltar que a nuvem deve ser configurada na maquina com IP: ```172.16.103.7```. Deve ser configurado 3 nevoas nas maquinas de seguinte IP: ```172.16.103.3```, ```172.16.103.5```, ```172.16.103.8```.

O repositório do dockerhub está no link: <a>https://hub.docker.com/repository/docker/diego10rocha/pbl2_redes/general</a>. E as imagens utilizadas foram:
- Nuvem: diego10rocha/pbl2_redes:nuvem_final
- Nevoa: diego10rocha/pbl2_redes:nevoa
- Posto: diego10rocha/pbl2_redes:posto_random
- Carro: diego10rocha/pbl2_redes:carro_random

Entretanto, vale ressaltar que apenas a nevoa não é inicializado junto ao container, para executa-la é necessário abrir o terminal do container e executar o comando:

```
$ python3 main.py
```

É necessário também ter uma imagem de um broker funcionando, para instalar basta fazer o pull da seguinte imagem:

```
$ docker pull eclipse-mosquitto
```
Após instalar a imagem do broker é necessário rodar fazendo as devidas configurações que podem ser feitas atraves do terminal executando o seguinte comando:
```
$ docker run -it -p 1883:1883 -p 1017:1017 -v mosquitto.conf:/mosquitto/config/mosquitto.conf eclipse-mosquitto
```
Visto que "1017:1017" é um exemplo de porta para o broker, entretanto o sistema está configurado para funcionar nas seguintes portas e ips de acordo com cada região:
- Região 1: 
    - IP: 172.16.103.3
    - Porta: 1017
- Região 2:
    - IP: 172.16.103.5
    - Porta: 8331
- Região 3:
    - IP: 172.16.103.8
    - Porta: 1888

Então uma imagem docker do brocker deve ser configurada em cada uma das respctivas maquinas identificadas pelo devido IP.
## Conclusão

Os sistemas desenvolvidos resolvem o problema proposto, entretanto há formas melhores de resolver o problema principalmente pensando no quesito de escalabilidade de sistemas. No sistema desenvolvido o carro recebe mensagens diretamente dos postos das regiões, entretante como a nevoa já faz um processamento de qual o melhor posto da respectiva região, uma alternativa a esta solução pensando em um desempenho melhor seria fazer com que a nevoa enviasse aos carros da sua região qual o melhor posto, eviando que cada carro processe a mesma informação e sobrecarregue o servidor MQTT responsável pela publicação de mensagens dos postos. Importante falar também que foi utilizado JSON para padronizar as mensagens trocadas via MQTT e nas requisições cliente-servidor via socket.

Link para testar os endpoints com o postman: https://www.postman.com/d1ego-cerqueira/workspace/sistemas-distribuidos
