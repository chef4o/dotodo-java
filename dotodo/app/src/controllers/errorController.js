import capitalize from "../util/capitalize";

export const formEmptyFieldsHandler = (
  formInitialState,
  formValues,
  addintionalFields,
  setValidationErrors
) => {
  if (emptyField(formValues)) {
    setValidationErrors((state) => ({
      ...state,
      ...addintionalFields,
    }));
    emptyFieldsErrorHandler(formValues, setValidationErrors);
  } else {
    setValidationErrors((state) => ({
      ...state,
      ...formInitialState,
    }));
  }
};

const emptyFieldsErrorHandler = (formValues, setValidationErrors) => {
  Object.keys(formValues).forEach((key) => {
    formValues[key].trim() == ""
      ? setValidationErrors((state) => ({
          ...state,
          [key]: `${capitalize(key)} is required`,
        }))
      : setValidationErrors((state) => ({ ...state, [key]: "" }));
  });
};

export const emptyField = (formValues) =>
  Object.values(formValues).some((value) => value.trim() === "");
