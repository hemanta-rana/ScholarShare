(function () {
    const form = document.getElementById('uploadForm');
    if (!form) return;

    let currentStep = 1;
    const totalSteps = 3;

    const panels = document.querySelectorAll('.upload-panel');
    const stepperItems = document.querySelectorAll('.stepper-item');
    const nextBtn = document.getElementById('nextBtn');
    const prevBtn = document.getElementById('prevBtn');
    const submitBtn = document.getElementById('submitBtn');
    const dropZone = document.getElementById('dropZone');
    const fileInput = document.getElementById('resourceFile');
    const browseBtn = document.getElementById('browseBtn');
    const fileSelected = document.getElementById('fileSelected');

    function showStep(step) {
        currentStep = step;
        panels.forEach(function (panel) {
            panel.classList.toggle('active', Number(panel.dataset.panel) === step);
        });
        stepperItems.forEach(function (item) {
            item.classList.toggle('active', Number(item.dataset.step) === step);
        });

        prevBtn.hidden = step === 1;
        nextBtn.hidden = step === totalSteps;
        submitBtn.hidden = step !== totalSteps;

        if (step === 1) {
            nextBtn.textContent = 'Proceed to Details';
        } else if (step === 2) {
            nextBtn.textContent = 'Proceed to Review';
        }

        if (step === totalSteps) {
            updateReview();
        }
    }

    function validateStep(step) {
        if (step === 1) {
            const title = document.getElementById('title');
            const file = fileInput;
            if (!title.value.trim()) {
                title.focus();
                alert('Please enter a resource title.');
                return false;
            }
            if (!file.files || file.files.length === 0) {
                alert('Please select a file to upload.');
                return false;
            }
            return true;
        }
        if (step === 2) {
            const description = document.getElementById('description');
            const type = document.getElementById('resourceType');
            const topic = document.getElementById('topicCategory');
            if (!description.value.trim()) {
                description.focus();
                alert('Please enter a description.');
                return false;
            }
            if (!type.value) {
                type.focus();
                alert('Please select a resource type.');
                return false;
            }
            if (!topic.value) {
                topic.focus();
                alert('Please select a topic category.');
                return false;
            }
            return true;
        }
        return true;
    }

    function updateReview() {
        const title = document.getElementById('title').value.trim();
        const course = document.getElementById('courseCode').value.trim() || '—';
        const description = document.getElementById('description').value.trim();
        const typeSelect = document.getElementById('resourceType');
        const topicSelect = document.getElementById('topicCategory');
        const typeText = typeSelect.options[typeSelect.selectedIndex]?.text || '—';
        const topicText = topicSelect.options[topicSelect.selectedIndex]?.text || '—';
        const fileName = fileInput.files[0]?.name || '—';

        document.getElementById('reviewTitle').textContent = title || '—';
        document.getElementById('reviewCourse').textContent = course;
        document.getElementById('reviewDescription').textContent = description || '—';
        document.getElementById('reviewType').textContent = typeText;
        document.getElementById('reviewTopic').textContent = topicText;
        document.getElementById('reviewFile').textContent = fileName;
    }

    function updateFileLabel() {
        if (fileInput.files && fileInput.files.length > 0) {
            fileSelected.hidden = false;
            fileSelected.textContent = 'Selected: ' + fileInput.files[0].name;
        } else {
            fileSelected.hidden = true;
            fileSelected.textContent = '';
        }
    }

    nextBtn.addEventListener('click', function () {
        if (!validateStep(currentStep)) return;
        if (currentStep < totalSteps) {
            showStep(currentStep + 1);
        }
    });

    prevBtn.addEventListener('click', function () {
        if (currentStep > 1) {
            showStep(currentStep - 1);
        }
    });

    stepperItems.forEach(function (item) {
        item.addEventListener('click', function () {
            const target = Number(item.dataset.step);
            if (target < currentStep) {
                showStep(target);
                return;
            }
            if (target > currentStep) {
                for (let s = currentStep; s < target; s++) {
                    if (!validateStep(s)) return;
                }
                showStep(target);
            }
        });
    });

    if (browseBtn && fileInput) {
        browseBtn.addEventListener('click', function (e) {
            e.preventDefault();
            e.stopPropagation();
            fileInput.click();
        });
    }

    if (fileInput) {
        fileInput.addEventListener('change', updateFileLabel);
    }

    if (dropZone) {
        dropZone.addEventListener('dragover', function (e) {
            e.preventDefault();
            dropZone.classList.add('dragover');
        });
        dropZone.addEventListener('dragleave', function () {
            dropZone.classList.remove('dragover');
        });
        dropZone.addEventListener('drop', function (e) {
            e.preventDefault();
            dropZone.classList.remove('dragover');
            if (e.dataTransfer.files.length > 0) {
                fileInput.files = e.dataTransfer.files;
                updateFileLabel();
            }
        });
    }

    form.addEventListener('submit', function (e) {
        if (currentStep !== totalSteps) {
            e.preventDefault();
            if (validateStep(1) && validateStep(2)) {
                showStep(totalSteps);
            }
            return;
        }
        if (!validateStep(1) || !validateStep(2)) {
            e.preventDefault();
        }
    });

    showStep(1);
})();
