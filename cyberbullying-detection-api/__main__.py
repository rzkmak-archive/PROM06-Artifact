from service.cyberbully_detection_service import CyberBullyDetectionService


def main() -> None:
    service = CyberBullyDetectionService()
    service.classify("This is a good cat and this is a bad dog.")


if __name__ == "__main__":
    main()
