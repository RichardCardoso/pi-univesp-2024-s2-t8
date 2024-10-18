window.onload = function() {
    initSelectAllCheckbox();
    initInativarButton();
    initToggleInativarButton();
    disableSelectAllIfAllInactive();
};

// Função para inicializar o comportamento do checkbox "Selecionar todas"
function initSelectAllCheckbox() {
    var selectAllCheckbox = document.getElementById('select-all');
    if (selectAllCheckbox) {
        selectAllCheckbox.onclick = function() {
            toggleCheckboxes(selectAllCheckbox.checked); // Seleciona/desmarca apenas seguradoras ativas
            toggleInativarButton(); // Atualiza o botão de inativação após a mudança
        };
    }
}

// Função para selecionar ou desmarcar todos os checkboxes ativos
function toggleCheckboxes(checkedState) {
    var checkboxes = document.getElementsByName('selectedSeguradoras');
    checkboxes.forEach(function(checkbox) {
        if (!checkbox.disabled) {  // Verifica se a seguradora está ativa
            checkbox.checked = checkedState;
        }
    });
}

// Função para inicializar o comportamento do botão "Inativar selecionados"
function initInativarButton() {
    var inativarButton = document.getElementById('inativarSelecionados');
    if (inativarButton) {
        inativarButton.onclick = function() {
            var selectedSeguradoras = getSelectedSeguradoras();
            if (selectedSeguradoras.length > 0) {
                if (confirm('Tem certeza que deseja inativar as seguradoras selecionadas?')) {
                    processClientRequest(selectedSeguradoras, 'DELETE', '/seguradoras?idsSeguradora=');
                }
            } else {
                alert('Nenhuma seguradora foi selecionada.');
            }
        };
    }
}

// Função para pegar as seguradoras selecionadas
function getSelectedSeguradoras() {
    return Array.from(document.querySelectorAll('input[name="selectedSeguradoras"]:checked')).map(function(seguradora) {
        return seguradora.value;
    });
}

// Função para enviar requisição de ativação ou inativação
function processClientRequest(ids, method, url) {
    fetch(`${url}${ids.join(',')}`, {
        method: method,
        headers: { 'Content-Type': 'application/json' }
    })
    .then(response => {
        if (response.ok) {
            alert(method === 'PUT' ? 'Seguradora ativada com sucesso.' : 'Seguradoras inativadas com sucesso.');
            location.reload(); // Recarrega a página após a ativação/inativação
        } else {
            alert(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) seguradora(s).`);
        }
    })
    .catch(error => {
        console.error(`Erro ao ${method === 'PUT' ? 'ativar' : 'inativar'} o(s) seguradora(s):`, error);
    });
}

// Função para verificar se algum checkbox está selecionado e atualizar a visibilidade do botão de inativação
function toggleInativarButton() {
    const inativarButton = document.getElementById('inativarSelecionados');
    const selectedSeguradoras = getSelectedSeguradoras();
    inativarButton.style.display = selectedSeguradoras.length > 0 ? 'inline-block' : 'none'; // Mostra/esconde o botão
}

// Função para inicializar a verificação de estado dos checkboxes de seguradoras
function initToggleInativarButton() {
    document.querySelectorAll('input[name="selectedSeguradoras"]').forEach(function(checkbox) {
        checkbox.addEventListener('change', toggleInativarButton);
    });
}

// Função para desabilitar o checkbox "Selecionar todos" se todas os seguradoras estiverem inativas
function disableSelectAllIfAllInactive() {
    const seguradoraCheckboxes = document.querySelectorAll('input[name="selectedSeguradoras"]');
    const allInactive = Array.from(seguradoraCheckboxes).every(checkbox => checkbox.disabled);
    const selectAllCheckbox = document.getElementById('select-all');

    selectAllCheckbox.disabled = allInactive;  // Desabilita o checkbox "Selecionar todos" se todas estiverem inativas
}

// Função para ativar seguradora
function ativarSeguradora(seguradoraId) {
    if (confirm('Tem certeza que deseja ativar o seguradora?')) {
        processClientRequest([seguradoraId], 'PUT', '/seguradoras/ativar?idsSeguradora=');
    }
}
