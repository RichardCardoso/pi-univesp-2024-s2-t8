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
            toggleCheckboxes(selectAllCheckbox.checked); // Seleciona/desmarca apenas servicos ativos
            toggleInativarButton(); // Atualiza o botão de inativação após a mudança
        };
    }
}

// Função para selecionar ou desmarcar todos os checkboxes ativos
function toggleCheckboxes(checkedState) {
    var checkboxes = document.getElementsByName('selectedServicos');
    checkboxes.forEach(function(checkbox) {
        if (!checkbox.disabled) {  // Verifica se o servico está ativo
            checkbox.checked = checkedState;
        }
    });
}

// Função para inicializar o comportamento do botão "Inativar selecionados"
function initInativarButton() {
    var inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            var selectedServicos = getSelectedServicos();
            if (selectedServicos.length > 0) {
                if (confirm('Tem certeza que deseja inativar os servicos selecionados?')) {
                    processServiceRequest(selectedServicos, 'DELETE', '/servicos?idsServico=');
                }
            } else {
                alert('Nenhum servico foi selecionado.');
            }
        };
    }
}

// Função para pegar os servicos selecionados
function getSelectedServicos() {
    return Array.from(document.querySelectorAll('input[name="selectedServicos"]:checked')).map(function(servico) {
        return servico.value;
    });
}

// Função para enviar requisição de ativação ou inativação
function processServiceRequest(ids, method, url) {
    fetch(`${url}${ids.join(',')}`, {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (response.ok) {
            alert(method === 'PUT' ? 'Servico ativado com sucesso.' : 'Servicos inativados com sucesso.');
            location.reload(); // Recarrega a página após a ativação/inativação
        } else {
            alert(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) servico(s).`);
        }
    })
    .catch(error => {
        console.error(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) servico(s):`, error);
    });
}

// Função para verificar se algum checkbox está selecionado e atualizar a visibilidade do botão de inativação
function toggleInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    const selectedServicos = getSelectedServicos();
    inativarButton.style.display = selectedServicos.length > 0 ? 'inline-block' : 'none'; // Mostra/esconde o botão
}

// Função para inicializar a verificação de estado dos checkboxes de servicos
function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedServicos"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleInativarButton);
    });
}

// Função para desabilitar o checkbox "Selecionar todos" se todos os servicos estiverem inativos
function disableSelectAllIfAllInactive() {
    const servicoCheckboxes = document.querySelectorAll('input[name="selectedServicos"]');
    const allInactive = Array.from(servicoCheckboxes).every(checkbox => checkbox.disabled);
    const selectAllCheckbox = document.getElementById('select-all');

    selectAllCheckbox.disabled = allInactive;  // Desabilita o checkbox "Selecionar todos" se todos estiverem inativos
}

// Função para ativar servico
function ativarServico(servicoId) {
    if (confirm('Tem certeza que deseja ativar o servico?')) {
        processServiceRequest([servicoId], 'PUT', '/servicos/ativar?idsServico=');
    }
}
