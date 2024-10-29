window.onload = function() {
    initSelectAllCheckbox();
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive();
};

// Função para inicializar o comportamento do checkbox "Selecionar todos"
function initSelectAllCheckbox() {
    var selectAllCheckbox = document.getElementById('select-all');
    if (selectAllCheckbox) {
        selectAllCheckbox.onclick = function() {
            toggleCheckboxes(selectAllCheckbox.checked); // Seleciona/desmarca apenas servicosAdquiridos ativos
            toggleInativarButton(); // Atualiza o botão de inativação após a mudança
        };
    }
}

// Função para selecionar ou desmarcar todos os checkboxes ativos
function toggleCheckboxes(checkedState) {
    var checkboxes = document.getElementsByName('selectedServicosAdquiridos');
    checkboxes.forEach(function(checkbox) {
        if (!checkbox.disabled) {  // Verifica se o servicoAdquirido está ativo
            checkbox.checked = checkedState;
        }
    });
}

// Função para inicializar o comportamento do botão "Inativar selecionados"
function initInativarButton() {
    var inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            var selectedServicosAdquiridos = getSelectedServicos();
            if (selectedServicosAdquiridos.length > 0) {
                if (confirm('Tem certeza que deseja inativar os servicosAdquiridos selecionados?')) {
                    processServiceRequest(selectedServicosAdquiridos, 'DELETE', '/servicos-adquiridos?idsServico=');
                }
            } else {
                alert('Nenhum servicoAdquirido foi selecionado.');
            }
        };
    }
}

// Função para pegar os servicosAdquiridos selecionados
function getSelectedServicos() {
    return Array.from(document.querySelectorAll('input[name="selectedServicosAdquiridos"]:checked')).map(function(servicoAdquirido) {
        return servicoAdquirido.value;
    });
}

// Função para enviar requisição de ativação ou inativação
function processServiceRequest(ids, method, url) {
    showLoader(); // Exibe o loader ao iniciar a requisição
    fetch(`${url}${ids.join(',')}`, {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (response.ok) {
            alert(method === 'PUT' ? '"Serviços adquiridos" ativados com sucesso.' : '"Servicos adquiridos" inativados com sucesso.');
            location.reload(); // Recarrega a página após a ativação/inativação
        } else {
            alert(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) servicoAdquirido(s).`);
        }
    })
    .catch(error => {
        console.error(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) servicoAdquirido(s):`, error);
    })
    .finally(() => {
        hideLoader(); // Esconde o loader após a requisição
    });
}

// Função para verificar se algum checkbox está selecionado e atualizar a visibilidade do botão de inativação
function toggleInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    const selectedServicosAdquiridos = getSelectedServicos();
    inativarButton.style.display = selectedServicosAdquiridos.length > 0 ? 'inline-block' : 'none'; // Mostra/esconde o botão
}

// Função para inicializar a verificação de estado dos checkboxes de servicosAdquiridos
function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedServicosAdquiridos"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleInativarButton);
    });
}

// Função para desabilitar o checkbox "Selecionar todos" se todos os servicosAdquiridos estiverem inativos
function disableSelectAllIfAllInactive() {
    const servicoCheckboxes = document.querySelectorAll('input[name="selectedServicosAdquiridos"]');
    const allInactive = Array.from(servicoCheckboxes).every(checkbox => checkbox.disabled);
    const selectAllCheckbox = document.getElementById('select-all');

    selectAllCheckbox.disabled = allInactive;  // Desabilita o checkbox "Selecionar todos" se todos estiverem inativos
}

// Função para ativar servicoAdquirido
function ativarServico(servicoId) {
    if (confirm('Tem certeza que deseja ativar o "Serviço Adquirido"?')) {
        showLoader(); // Exibe o loader antes de iniciar a ativação
        processServiceRequest([servicoId], 'PUT', '/servicos-adquiridos/ativar?idsServicoAdquirido=');
    }
}
