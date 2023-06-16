# MI Concorrência e Conectividade: Transações Bancárias Distribuidas
O objetivo deste projeto foi a implementação de um sistema distribuído para transações bancárias e utilização de algoritmos para resolver o problema de concorrencia dos dados.

* * *

## Autores: 
  * Carlos Valadão [@arlosValadao](https://github.com/arlosValadao)
  * Diego Cerqueira [@Diego10Rocha](https://github.com/Diego10Rocha)

* * *

## Seções 

&nbsp;&nbsp;&nbsp;[**1.** Introdução](#introducao)

&nbsp;&nbsp;&nbsp;[**2.** Fundamentação Teórica](#fundamentacao_teorica)

&nbsp;&nbsp;&nbsp;[**3.** Metodologia](#metodologia)

&nbsp;&nbsp;&nbsp;[**4.** Resultados](#resultados)

&nbsp;&nbsp;&nbsp;[**5.** Conclusão](#conclusao)

&nbsp;&nbsp;&nbsp;[**6.** Referências](#referencias)


# <a id="introducao"></a>
## Introdução

A presença de uma entidade centralizadora responsável por implementar leis e direitos em um sistema monetário
na grande maioria dos países atuais, comumente chamada de banco central, tem também como função a supervisão do sistema financeiro e execução da política monetária e cambial de um determinado país, de tal forma que a inexistência desta entidade em um país/localidade acarreta dentre outras desvantagens um sistema financeiro não supervisionado, e descentralizado, cabendo as entidades existentes garantirem o supervisionamento do sistema financeiro e a execução da política monetária e cambial, dentre outras atribuições.

# <a id="fundamentacao_teorica"></a>
## Fundamentação Teórica

### Token Ring
A comunicação entre processos é um dos elementos essenciais de um sistema pois, permite que informações sejam compartilhadas e novos conhecimentos sejam gerados, tão importante quanto a comunicação é a sincronização destes, enquanto comunicam-se, principalmente em sistemas com natureza distribuída e em sistemas construídos de forma centralizada.

Enquanto em sistemas centralizados, regiões críticas, exclusão mútua e demais problemas de sincronização são resolvidos por meio de semáforos. Contudo em sistemas distribuídos, àqueles em que não são executados em uma única CPU, estas abordagens são ineficazes, devida a dificuldade ao determinar corretamente a ordem dos eventos gerados pelos membros deste sistema distribuído. Portanto, abordagens de sincronização em sistemas distribuídos visam definir uma ordem, cronológica ou não, dos eventos ocorridos em um dado sistema não centralizado, uma das mais conhecidas e utilizada na implementação da solução aqui apresentada foi o método Token Ring.
O algoritmo de Token Ring é uma forma de obter a exclusão mútua entre processos de sistemas distribuídos, ele é representado por um barramento (comumente Ethernet), sem ordenação de processos, sendo totalmente implementado em software, um anel é construído, e a cada elemento é atribuída uma posição neste anel, sendo as posições podendo respeitar algum critério de precedência, o endereço IP, por exemplo. No Token Ring a ordem dos seus componentes é irrelevante, uma vez basta apenas o nó atual conhecer o seu próximo nó imediato no anel, a quem irá passar o token. O token é uma chave que garante ao portador dele acesso irrestrito à região crítica, contudo apenas uma vez a cada recebimento do token, passando o token para o seu vizinho logo após acessar a região crítica.
Um nó recebe o Token logo que o anel é inicializado, o token circula pelo anel, passando do processo A para o processo A + 1. Caso um nó do anel possua o token e não esteja interessado em ingressar na região crítica, este passa o token para o seu vizinho imediato, logo quando nenhum elemento do anel deseje entrar na região crítica, o token permanece circulando até que algum nó possua interesse em adentar a região crítica.
Por outro lado, caso algum nó do anel cair, todo ele ficará comprometido, bem como é uma tarefa difícil identificar quando ocorreu ou se houve a perda do token, se isso ocorrer um novo token deve ser gerado. Por outro lado, neste algoritmo é impossível ocorrer starvation, uma vez que o token circula em ordem bem-definida.

# <a id="metodologia"></a>
## Metodologia
Para solucionar o problema proposto, primeiro foram iniciados os estudos para entender como funciona os algoritmos distribuídos que buscam resolvel os problemas de concorrencia. A principio foi estudado os algoritmos de Lamport, Lamport com multicast, Token Ring, Exclusão mutua e relogio vetorial. Após os estudos foi feito o desenvolvimento do mesmo e depois de funcionando foi iniciado o desenvolvimento do sistena proposto. Ao finalizar o desenvolvimento do sistema solicitado, foi pesquisado como criar uma imagem docker da aplicação desenvolvida e executá-la dentro de um container.

A linguagem de programação escolhida para a solução do problema foi Java com o uso do Spring Framework para construção das APIs Rest.

Foi utilizado como padrão para tráfego de dados o  formato JSON(Javascript Object Notation) para comunicação das apis.

Para testar as aplicações foi feito a utilização de conteiners docker e do postman para testar as APIs.

# <a id="resultados"></a>
## Resultados
A solução do problema contou com 4 servidores de bancos distintos formando uma rede em anel. Após subir os servidores há um endpoint para incialização do algoritmo do token ring. Para tratar da concorrência nas requisições dentro do próprio servidor foi utilizado semaforo para cada conta. As transações então podem ocorrer sendo as requisições disparadas de modo simultâneo que já foi feito o tratamento de concorrência e caso há algum problema em uma requisição, as alterações feitas por ela até o momento do erro são desfeitas.

### Servidor do Banco
Foram desenvolvido 4 servidores de bancos, entretanto a parte lógica ficou duplicada, mudando apenas a lógica para construção da rede em anel. Através de cada servidor é possível realizar o cadastro de contas novas com a restrição de que o número da conta não pode ser repetido. É possível realizar saques, depositos e consultas nos servidores além da funcionalidade de transferências que permite que o cliente faça transações de n contas de origem para uma conta de destino, visto que a conta de destino pode estar em qualquer servidor e as contas de origem podem estar em qualquer servidor também contanto que o cliente tenha conta no servidor do banco no qual ele requisitou a transação. Foi feito também o tratamento de exceções e possíveis falhas no que tinge as transferências, então os casos de a conta de origem informada não existir, conta de destino não desistir, saldo insuficiente, foram todos validados e dão uma resposta bem detalhada a requisição da API. Quanto ao algoritmo de token ring ele não é tolerante a falhas, então caso o servidor de um dos bancos caia o sistema fica indisponível para as operações que editem o valor do saldo das contas, pois o token do algoritmo foi perdido já que a rede em anel foi desfeita.

Link para testar os endpoints com o postman: https://www.postman.com/d1ego-cerqueira/workspace/sistemas-distribuidos

### Docker
Foi criado uma imagem docker para cada um dos 4 servidores dos bancos e estas imagens foram disponibilizadas no repositório online do docker hub(``diego10rocha/pbl3``). 
Para poder ter estas aplicações executando em um computador basta alterar o nome da imagem para o nome correspondente no dockerhub no arquivo docker-compose.yml, após isso basta abrir no terminal a pasta raiz do sistema desejado e executar o seguinte comando:


```
$ docker compose up
```

É importante ressaltar que os servidores estejam em maquinas especificas para que o token ring funcione. Então o servidor intitulado "Banco do Brasil" deve ser configurada na maquina com IP: ```172.16.103.1```, o servidor intitulado "Caixa Economica" deve ser configurada na maquina: ```172.16.103.2```, o servidor intitulado "Santander" deve ser configurada na maquina: ```172.16.103.3```, o servidor intitulado "Itau" deve ser configurada na maquina: ```172.16.103.4```.

O repositório do dockerhub está no link: <a>https://hub.docker.com/repository/docker/diego10rocha/pbl3_redes/general</a>. E as imagens utilizadas foram:
- Banco do Brasil: diego10rocha/pbl3_redes:banco_do_brasil_bridge
- Caixa Economica: diego10rocha/pbl3_redes:caixa_economica_bridge
- Santander: diego10rocha/pbl3_redes:santander_bridge
- Itau: diego10rocha/pbl3_redes:itau_bridge

Após fazer a instalação da imagem, basta rodar que os servidores já estarão funcionando.

# <a id="conclusao"></a>
## Conclusão

O sistema desenvolvido atende aos requisitos do problema proposto, trata as possíveis falhas, problemas de concorrência tanto de requisições de um próprio servidor, quanto requisições entre servidores, entretanto o algoritmo distribuído token ring não é toletrante a falhas, então basta um servidor cair para a aplicação para de funcionar corretamente. Um ponto de melhoria seria trata a quebra da rede em anel por perda de token quando um dos servidores cair. Um ponto positivo foi a utilização do Spring Framework para o desenvolvimento da aplicação e das APIs.


# <a id="referencias"></a>
## Referências
