# Problema 3 - Transações Bancárias Distribuídas

* * *

## Autores: 
  * Carlos Valadão [@arlosValadao](https://github.com/arlosValadao)
  * Diego Cerqueira [@Diego10Rocha](https://github.com/Diego10Rocha)

* * *

## Seções 

&nbsp;&nbsp;&nbsp;[**1.** Introdução](#introducao)

&nbsp;&nbsp;&nbsp;[**2.** Fundamentação Teórica](#fundamentacao_teorica)

&nbsp;&nbsp;&nbsp;[**3.** Caracterização da Solução](#caracterizacao_solucao)

&nbsp;&nbsp;&nbsp;[**4.** Conclusão](#conclusao)

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

# <a id="caracterizacao_solucao"></a>
## Caracterização da Solução

# <a id="conclusao"></a>
## Conclusão

# <a id="referencias"></a>
## Referências
