
// UC15: Service contract.
//
// Every method receives DTO input and returns
// a standardized operation entity.
public interface IQuantityMeasurementService {

    QuantityMeasurementEntity compare(
            QuantityDTO first,
            QuantityDTO second);

    QuantityMeasurementEntity convert(
            QuantityDTO quantity,
            String targetUnit);

    QuantityMeasurementEntity add(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit);

    QuantityMeasurementEntity subtract(
            QuantityDTO first,
            QuantityDTO second,
            String targetUnit);

    QuantityMeasurementEntity divide(
            QuantityDTO first,
            QuantityDTO second);
}
