// URLs das APIs
const API_URL = '/api/emprestimos';
const API_COLABORADORES = '/api/colaboradores';
const API_EPIS = '/api/epis';

// funcao pra mostrar mensagem
function mostrarMensagem(texto, tipo) {
    var div = document.getElementById('mensagem');
    div.innerHTML = '<div class="message message-' + tipo + '">' + texto + '</div>';
    setTimeout(function() {
        div.innerHTML = '';
    }, 5000);
}

// formata data
function formatarData(data) {
    if (!data) return '-';
    var d = new Date(data + 'T00:00:00');
    return d.toLocaleDateString('pt-BR');
}

// retorna o HTML do badge de status
function statusBadge(status) {
    if (status === 'ATIVO') {
        return '<span class="badge badge-warning">Ativo</span>';
    } else if (status === 'DEVOLVIDO') {
        return '<span class="badge badge-success">Devolvido</span>';
    } else if (status === 'ATRASADO') {
        return '<span class="badge badge-danger">Atrasado</span>';
    }
    return status;
}

// carrega a lista de emprestimos
async function carregarEmprestimos() {
    try {
        var response = await fetch(API_URL);
        var data = await response.json();

        var tbody = document.getElementById('tabelaEmprestimos');

        if (data.length === 0) {
            tbody.innerHTML = '<tr><td colspan="8" style="text-align:center;color:#999;">Nenhum empréstimo registrado</td></tr>';
            return;
        }

        var html = '';
        for (var i = 0; i < data.length; i++) {
            var emp = data[i];

            // pega nome do colaborador e do epi com seguranca
            var nomeColaborador = emp.colaborador ? emp.colaborador.nome : '-';
            var nomeEpi = emp.epi ? emp.epi.nome : '-';

            html += '<tr>';
            html += '<td>' + emp.id + '</td>';
            html += '<td>' + nomeColaborador + '</td>';
            html += '<td>' + nomeEpi + '</td>';
            html += '<td>' + formatarData(emp.dataRetirada) + '</td>';
            html += '<td>' + formatarData(emp.dataPrevistaDevolucao) + '</td>';
            html += '<td>' + formatarData(emp.dataDevolucao) + '</td>';
            html += '<td>' + statusBadge(emp.status) + '</td>';
            html += '<td>';

            // se ta ativo, mostra botao de devolver
            if (emp.status === 'ATIVO') {
                html += '<button class="btn btn-success btn-sm" onclick="devolver(' + emp.id + ')">Devolver</button> ';
            }
            html += '<button class="btn btn-warning btn-sm" onclick="editar(' + emp.id + ')">Editar</button> ';
            html += '<button class="btn btn-danger btn-sm" onclick="excluir(' + emp.id + ')">Excluir</button>';
            html += '</td>';
            html += '</tr>';
        }
        tbody.innerHTML = html;

    } catch (error) {
        document.getElementById('tabelaEmprestimos').innerHTML =
            '<tr><td colspan="8" style="text-align:center;color:#c62828;">Erro ao carregar dados</td></tr>';
    }
}

// carrega os selects do formulario com dados da API
async function carregarSelects() {
    try {
        // faz as duas requisicoes ao mesmo tempo
        var colResponse = await fetch(API_COLABORADORES);
        var epiResponse = await fetch(API_EPIS);

        var colaboradores = await colResponse.json();
        var epis = await epiResponse.json();

        // preenche select de colaboradores
        var selectCol = document.getElementById('colaborador');
        var htmlCol = '<option value="">Selecione...</option>';
        for (var i = 0; i < colaboradores.length; i++) {
            htmlCol += '<option value="' + colaboradores[i].id + '">' + colaboradores[i].nome + ' (CPF: ' + colaboradores[i].cpf + ')</option>';
        }
        selectCol.innerHTML = htmlCol;

        // preenche select de EPIs
        var selectEpi = document.getElementById('epi');
        var htmlEpi = '<option value="">Selecione...</option>';
        for (var j = 0; j < epis.length; j++) {
            htmlEpi += '<option value="' + epis[j].id + '">' + epis[j].nome + ' (Estoque: ' + epis[j].quantidadeEstoque + ')</option>';
        }
        selectEpi.innerHTML = htmlEpi;

    } catch (error) {
        mostrarMensagem('Erro ao carregar dados para o formulário', 'error');
    }
}

// registra a devolucao
async function devolver(id) {
    if (!confirm('Confirmar devolução deste EPI?')) {
        return;
    }

    try {
        var response = await fetch(API_URL + '/' + id + '/devolver', { method: 'PUT' });
        var result = await response.json();

        if (response.ok) {
            mostrarMensagem('Devolução registrada com sucesso', 'success');
            carregarEmprestimos();
        } else {
            mostrarMensagem(result.erro || 'Erro ao registrar devolução', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
}

function abrirModal() {
    document.getElementById('modalTitulo').textContent = 'Novo Empréstimo';
    document.getElementById('formEmprestimo').reset();
    document.getElementById('emprestimoId').value = '';
    document.getElementById('grupoDataDevolucao').style.display = 'none';
    document.getElementById('grupoStatus').style.display = 'none';

    // seta a data atual como data de retirada
    var hoje = new Date().toISOString().split('T')[0];
    document.getElementById('dataRetirada').value = hoje;

    carregarSelects();
    document.getElementById('modal').classList.add('active');
}

function fecharModal() {
    document.getElementById('modal').classList.remove('active');
}

async function editar(id) {
    try {
        var response = await fetch(API_URL + '/' + id);
        var emp = await response.json();

        document.getElementById('modalTitulo').textContent = 'Editar Empréstimo';
        document.getElementById('emprestimoId').value = emp.id;

        await carregarSelects();

        document.getElementById('colaborador').value = emp.colaborador ? emp.colaborador.id : '';
        document.getElementById('epi').value = emp.epi ? emp.epi.id : '';
        document.getElementById('dataRetirada').value = emp.dataRetirada || '';
        document.getElementById('dataPrevistaDevolucao').value = emp.dataPrevistaDevolucao || '';
        document.getElementById('dataDevolucao').value = emp.dataDevolucao || '';
        document.getElementById('status').value = emp.status;

        // mostra campos extras se ja foi devolvido
        if (emp.status === 'DEVOLVIDO') {
            document.getElementById('grupoDataDevolucao').style.display = 'block';
            document.getElementById('grupoStatus').style.display = 'block';
        } else {
            document.getElementById('grupoDataDevolucao').style.display = 'none';
            document.getElementById('grupoStatus').style.display = 'none';
        }

        document.getElementById('modal').classList.add('active');
    } catch (error) {
        mostrarMensagem('Erro ao carregar dados do empréstimo', 'error');
    }
}

async function excluir(id) {
    if (!confirm('Tem certeza que deseja excluir este empréstimo?')) {
        return;
    }

    try {
        var response = await fetch(API_URL + '/' + id, { method: 'DELETE' });
        var result = await response.json();

        if (response.ok) {
            mostrarMensagem(result.mensagem || 'Empréstimo removido com sucesso', 'success');
            carregarEmprestimos();
        } else {
            mostrarMensagem(result.erro || 'Erro ao remover empréstimo', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
}

// evento do formulario
document.getElementById('formEmprestimo').addEventListener('submit', async function(e) {
    e.preventDefault();

    var id = document.getElementById('emprestimoId').value;

    var dados = {
        colaborador: { id: parseInt(document.getElementById('colaborador').value) },
        epi: { id: parseInt(document.getElementById('epi').value) },
        dataRetirada: document.getElementById('dataRetirada').value,
        dataPrevistaDevolucao: document.getElementById('dataPrevistaDevolucao').value,
        dataDevolucao: document.getElementById('dataDevolucao').value || null
    };

    // se for edicao, inclui o status
    if (id) {
        dados.status = document.getElementById('status').value;
    }

    try {
        var url = id ? API_URL + '/' + id : API_URL;
        var method = id ? 'PUT' : 'POST';

        var response = await fetch(url, {
            method: method,
            headers: { 'Content-Type': 'application/json' },
            body: JSON.stringify(dados)
        });

        var result = await response.json();

        if (response.ok) {
            var msg = id ? 'Empréstimo atualizado com sucesso' : 'Empréstimo registrado com sucesso';
            mostrarMensagem(msg, 'success');
            fecharModal();
            carregarEmprestimos();
        } else {
            mostrarMensagem(result.erro || 'Erro ao salvar empréstimo', 'error');
        }
    } catch (error) {
        mostrarMensagem('Erro ao conectar com o servidor', 'error');
    }
});

// carrega a pagina
carregarEmprestimos();
